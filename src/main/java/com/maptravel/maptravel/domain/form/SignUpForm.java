package com.maptravel.maptravel.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignUpForm {

  private String email;

  private String password;

  private String name;

  private String nickname;

}
