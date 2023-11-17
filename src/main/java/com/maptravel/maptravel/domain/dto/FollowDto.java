package com.maptravel.maptravel.domain.dto;

import java.time.LocalDateTime;
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
public class FollowDto {

  private Long id;

  private String userNickname;

  private String userProfileImageUrl;

  private LocalDateTime createdAt;

}
