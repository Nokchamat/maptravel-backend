package com.maptravel.maptravel.oauth.info;

import com.maptravel.maptravel.domain.constants.ProviderType;
import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

  private Map<String, Object> attributes; // getAttributes()

  public NaverUserInfo(Map<String, Object> attributes) {
    this.attributes = (Map<String, Object>) attributes.get("response");
  }

  @Override
  public String getProviderId() {
    return (String) attributes.get("id");
  }

  @Override
  public String getProvider() {
    return ProviderType.NAVER.name();
  }

  @Override
  public String getEmail() {
    return attributes.get("email").toString();
  }

  @Override
  public String getName() {
    return attributes.get("name").toString();
  }
}
