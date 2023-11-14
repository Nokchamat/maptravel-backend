package com.maptravel.maptravel.domain.entity;

import com.maptravel.maptravel.domain.constants.RoleType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String password;

  @Column(unique = true)
  private String nickname;

  private String name;

  private String profileImageUrl;

  private String emailVerifyCode;

  private Boolean isEmailVerify;

  @Enumerated(EnumType.STRING)
  private RoleType role;

  private String provider;

  private String providerId;

}
