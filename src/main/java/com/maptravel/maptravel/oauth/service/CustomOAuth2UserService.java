package com.maptravel.maptravel.oauth.service;

import com.maptravel.maptravel.domain.constants.RoleType;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.oauth.domain.PrincipalDetails;
import com.maptravel.maptravel.oauth.info.GoogleUserInfo;
import com.maptravel.maptravel.oauth.info.NaverUserInfo;
import com.maptravel.maptravel.oauth.info.OAuth2UserInfo;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("[loadUser] 동작");
    OAuth2User oAuth2User = super.loadUser(userRequest);

    OAuth2UserInfo oAuth2UserInfo;

    if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
      //google
      oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
    } else {
      //naver
      oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
    }

    String provider = Objects.requireNonNull(oAuth2UserInfo).getProvider();
    String providerId = Objects.requireNonNull(oAuth2UserInfo).getProviderId();
    String name = oAuth2UserInfo.getName();
    String email = oAuth2UserInfo.getEmail();

    Optional<User> userEntity = userRepository.findByEmail(email);

    if (userEntity.isEmpty()) {
      userEntity = Optional.ofNullable(User.builder()
          .name(name)
          .password("")
          .nickname(name + UUID.randomUUID())
          .email(email)
          .role(RoleType.USER)
          .provider(provider)
          .providerId(providerId)
          .isEmailVerify(true)
          .build());

      userRepository.save(userEntity.get());
    }

    return new PrincipalDetails(userEntity.get(), oAuth2User.getAttributes());
  }
}