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
public class BookmarkDto {

  private Long id;

  private Long planeId;

  private String subject;

  private String content;

  private String thumbnailUrl;

  private Long viewCount;

  private String userNickname;

  private String userProfileUrl;

}
