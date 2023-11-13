package com.maptravel.maptravel.oauth.info;

public interface OAuth2UserInfo {
  String getProviderId();
  String getProvider();
  String getEmail();
  String getName();
}