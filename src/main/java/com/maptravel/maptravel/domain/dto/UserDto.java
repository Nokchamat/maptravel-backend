package com.maptravel.maptravel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

  private String nickname;

  private String profileImageUrl;

  private Boolean isEmailVerify;

  private Long followerCount;

}
