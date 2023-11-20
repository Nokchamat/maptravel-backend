package com.maptravel.maptravel.domain.dto;

import com.maptravel.maptravel.domain.entity.Plane;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaneDto {

  private Long id;

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

  List<PlaceDto> placeDtoList;

  public static PlaneDto fromEntity(Plane plane, List<PlaceDto> placeDtoList) {
    return PlaneDto.builder()
        .id(plane.getId())
        .subject(plane.getSubject())
        .content(plane.getContent())
        .country(plane.getCountry())
        .city(plane.getCity())
        .viewCount(plane.getViewCount())
        .thumbnailUrl(plane.getThumbnailUrl())
        .userNickname(plane.getUser().getNickname())
        .userProfileImageUrl(plane.getUser().getProfileImageUrl())
        .isBookmark(false)
        .isLikes(false)
        .placeDtoList(placeDtoList)
        .build();
  }

  public void setLikes(Boolean likes) {
    isLikes = likes;
  }

  public void setBookmark(Boolean bookmark) {
    isBookmark = bookmark;
  }
}
