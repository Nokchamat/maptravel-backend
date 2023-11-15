package com.maptravel.maptravel.domain.dto;

import com.maptravel.maptravel.domain.entity.Place;
import com.maptravel.maptravel.domain.entity.Plane;
import java.util.List;
import java.util.stream.Collectors;
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

  private Long viewCount;

  private String thumbnailUrl;

  private String userNickname;

  private String userProfileImageUrl;

  List<PlaceDto> placeDtoList;
  
  //TODO 좋아요 여부 추가해야함

  public static PlaneDto fromEntity(Plane plane, List<PlaceDto> placeDtoList) {
    return PlaneDto.builder()
        .id(plane.getId())
        .subject(plane.getSubject())
        .content(plane.getContent())
        .viewCount(plane.getViewCount())
        .thumbnailUrl(plane.getThumbnailUrl())
        .userNickname(plane.getUser().getNickname())
        .userProfileImageUrl(plane.getUser().getProfileImageUrl())
        .placeDtoList(placeDtoList)
        .build();
  }

}
