package com.maptravel.maptravel.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoogleSignInForm {

  private String displayName;

  private String email;

  private String id;

}
