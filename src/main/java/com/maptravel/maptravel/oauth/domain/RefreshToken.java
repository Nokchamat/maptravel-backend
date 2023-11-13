package com.maptravel.maptravel.oauth.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class RefreshToken {

  @Id
  private String refreshToken;

  private String ip;

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
