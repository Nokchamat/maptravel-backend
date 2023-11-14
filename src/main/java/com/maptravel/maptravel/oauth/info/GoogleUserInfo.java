package com.maptravel.maptravel.oauth.info;

import com.maptravel.maptravel.domain.constants.ProviderType;
import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{

  private Map<String, Object> attributes; // getAttributes()

  public GoogleUserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String getProviderId() {
    return (String) attributes.get("sub");
  }

  @Override
  public String getProvider() {
    return ProviderType.GOOGLE.name();
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
