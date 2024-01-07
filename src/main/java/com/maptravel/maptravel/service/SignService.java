package com.maptravel.maptravel.service;

import com.maptravel.maptravel.domain.constants.ProviderType;
import com.maptravel.maptravel.domain.constants.RoleType;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.form.GoogleSignInForm;
import com.maptravel.maptravel.domain.form.SignInForm;
import com.maptravel.maptravel.domain.form.SignUpForm;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import com.maptravel.maptravel.oauth.domain.RefreshToken;
import com.maptravel.maptravel.oauth.domain.RefreshTokenRepository;
import com.maptravel.maptravel.oauth.domain.Token;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import com.maptravel.maptravel.oauth.service.ClientIp;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final SendEmailService sendEmailService;
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional
  public void signUp(SignUpForm signUpForm) {

    userRepository.findByEmail(signUpForm.getEmail())
        .ifPresent(user -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
        });

    userRepository.findByNickname(signUpForm.getNickname())
        .ifPresent(user -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_NICKNAME);
        });

    String emailVerifyCode = RandomString.make(5);

    userRepository.save(User.builder()
        .email(signUpForm.getEmail())
        .password(passwordEncoder.encode(signUpForm.getPassword()))
        .name(signUpForm.getName())
        .nickname(signUpForm.getNickname())
        .emailVerifyCode(emailVerifyCode)
        .isEmailVerify(false)
        .role(RoleType.USER)
        .provider(ProviderType.LOCAL.name())
        .build());

    sendEmailService.sendMail(signUpForm.getEmail(), emailVerifyCode);
  }

  public Token signIn(SignInForm signInForm, HttpServletRequest request) {
    User user = userRepository.findByEmail(signInForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.MISMATCH_EMAIL_PASSWORD));

    if (!passwordEncoder.matches(signInForm.getPassword(),
        user.getPassword())) {
      throw new CustomException(ErrorCode.MISMATCH_EMAIL_PASSWORD);
    }

    Token token = jwtTokenProvider.generateToken(signInForm.getEmail());
    refreshTokenRepository.save(
        RefreshToken.builder()
            .refreshToken(token.getRefreshToken())
            .ip(ClientIp.getClientIp(request))
            .build());

    return token;
  }

  public Token signInByGoogle(GoogleSignInForm signInForm, HttpServletRequest request) {
    Optional<User> user = userRepository.findByEmail(signInForm.getEmail());

    if (user.isEmpty()) {
      userRepository.save(User.builder()
          .email(signInForm.getEmail())
          .password(passwordEncoder.encode(
              signInForm.getEmail() + signInForm.getId()))
          .nickname(signInForm.getDisplayName() + signInForm.getEmail())
          .name(signInForm.getDisplayName())
          .profileImageUrl("")
          .emailVerifyCode("")
          .isEmailVerify(true)
          .role(RoleType.USER)
          .provider(ProviderType.GOOGLE.name())
          .providerId(signInForm.getId())
          .build());
    } else {
      if (!user.get().getProvider().equals(ProviderType.GOOGLE.name()) ||
          !user.get().getProviderId().equals(signInForm.getId()) ||
          !user.get().getName().equals(signInForm.getDisplayName())) {
        throw new CustomException(ErrorCode.ALREADY_EXIST_OTHER_PLATFORM);
      }
    }

    Token token = jwtTokenProvider.generateToken(signInForm.getEmail());
    refreshTokenRepository.save(
        RefreshToken.builder()
            .refreshToken(token.getRefreshToken())
            .ip(ClientIp.getClientIp(request))
            .build());

    return token;
  }

}
