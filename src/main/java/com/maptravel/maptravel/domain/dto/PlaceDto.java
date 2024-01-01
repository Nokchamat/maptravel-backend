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
public class PlaceDto {

  private Long id;

  private String subject;

  private String content;

  private String address;

  private Double latitude;

  private Double longitude;

  private String[] pictureUrlArray;

}
