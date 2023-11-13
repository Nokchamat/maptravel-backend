package com.maptravel.maptravel.oauth.domain;

import com.maptravel.maptravel.domain.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User { // OAuth2User 추가

  private User user; // 컴포지션
  private Map<String, Object> attributes; // 추가됨

  // 일반 로그인
  public PrincipalDetails(User user) {
    this.user = user;
  }

  // OAuth 로그인 // 추가됨
  public PrincipalDetails(User user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  @Override // 추가됨
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  // 해당 User의 권한을 리턴하는 곳!
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collect = new ArrayList<>();
    collect.add((GrantedAuthority) () -> user.getRole().getKey());

    return collect;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {

    // 사이트에서 1년동안 회원이 로그인을 안했을 경우 휴면 계정으로 하기로 함.
    // 현재 시간 - 로그인 시간 => 1년을 초과하면 return false;

    return true;
  }

  @Override // 추가됨 -- 사용은 X
  public String getName() {
    return null;
  }
}