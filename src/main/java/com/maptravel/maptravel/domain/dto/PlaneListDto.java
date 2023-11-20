package com.maptravel.maptravel.domain.dto;

import com.maptravel.maptravel.domain.entity.Plane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaneListDto {

  private Long planeId;

  private String subject;

  private String content;

  private String country;

  private String city;

  private Long viewCount;

  private String thumbnailUrl;

  private String userNickname;

  private String userProfileImageUrl;

  private Boolean isLikes;

  private Boolean isBookmark;

  public static PlaneListDto fromEntity(Plane plane) {

    return PlaneListDto.builder()
        .planeId(plane.getId())
        .subject(plane.getSubject())
        .content(plane.getContent())
        .country(plane.getCountry())
        .city(plane.getCity())
        .viewCount(plane.getViewCount())
        .thumbnailUrl(plane.getThumbnailUrl())
        .userNickname(plane.getUser().getNickname())
        .userProfileImageUrl(plane.getUser().getProfileImageUrl())
        .build();
  }

  public void setLikes(Boolean likes) {
    isLikes = likes;
  }

  public void setBookmark(Boolean bookmark) {
    isBookmark = bookmark;
  }

}

