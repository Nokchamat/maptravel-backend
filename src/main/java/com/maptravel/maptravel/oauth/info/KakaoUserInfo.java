//package com.maptravel.maptravel.oauth.info;
//
//import com.maptravel.maptravel.domain.constants.ProviderType;
//import java.util.Map;
//
//public class KakaoUserInfo implements OAuth2UserInfo{
//
//  private Map<String, Object> attributes; // getAttributes()
//  private Map<String, Object> kakaoAccount; // getAttributes()
//  private Map<String, Object> kakaoProfile; // getAttributes()
//
//  public KakaoUserInfo(Map<String, Object> attributes) {
//    this.attributes = attributes;
//    this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//    this.kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
//  }
//
//  @Override
//  public String getProviderId() {
//    return kakaoAccount.get("email").toString();
//  }
//
//  @Override
//  public String getProvider() {
//    return ProviderType.KAKAO.name();
//  }
//
//  @Override
//  public String getEmail() {
//    return kakaoAccount.get("email").toString();
//  }
//
//  @Override
//  public String getName() {
//    return kakaoProfile.get("nickname").toString();
//  }
//}
