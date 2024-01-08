package com.maptravel.maptravel.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Place extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String subject;

  @Lob
  private String content;

  private String address;

  @Column(columnDefinition = "varchar(30)")
  private Double latitude;

  @Column(columnDefinition = "varchar(30)")
  private Double longitude;

  @Lob
  private String pictureListUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plane_id")
  private Plane plane;

}
