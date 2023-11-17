package com.maptravel.maptravel.service;

import com.maptravel.maptravel.domain.constants.ProviderType;
import com.maptravel.maptravel.domain.constants.RoleType;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.form.SignInForm;
import com.maptravel.maptravel.domain.form.SignUpForm;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import com.maptravel.maptravel.oauth.domain.Token;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
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

  public Token signIn(SignInForm signInForm) {

    userRepository.findByEmail(signInForm.getEmail())
        .ifPresent(user -> {
          if (!passwordEncoder.matches(signInForm.getPassword(),
              user.getPassword())) {
            throw new CustomException(ErrorCode.MISMATCH_EMAIL_PASSWORD);
          }
        });

    return jwtTokenProvider.generateToken(signInForm.getEmail());
  }
}
