package com.maptravel.maptravel.controller;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

import com.maptravel.maptravel.domain.form.GoogleSignInForm;
import com.maptravel.maptravel.domain.form.SignInForm;
import com.maptravel.maptravel.domain.form.SignUpForm;
import com.maptravel.maptravel.oauth.domain.Token;
import com.maptravel.maptravel.service.SignService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class SignController {

  private final SignService signService;

  @PostMapping("/signup")
  ResponseEntity<Void> signUp(@RequestBody SignUpForm signUpForm) {

    signService.signUp(signUpForm);

    return ResponseEntity.ok(null);
  }

  @PostMapping("/signin")
  ResponseEntity<Void> signIn(@RequestBody SignInForm signInForm,
      HttpServletRequest request) {

    Token token = signService.signIn(signInForm, request);
    HttpHeaders headers = new HttpHeaders();
    headers.add(ACCESS_TOKEN, token.getAccessToken());
    headers.add(REFRESH_TOKEN, token.getRefreshToken());

    return ResponseEntity.ok().headers(headers).body(null);
  }

  @PostMapping("/signin/google")
  ResponseEntity<Void> signInByGoogle(@RequestBody GoogleSignInForm signInForm,
      HttpServletRequest request) {

    Token token = signService.signInByGoogle(signInForm, request);
    HttpHeaders headers = new HttpHeaders();
    headers.add(ACCESS_TOKEN, token.getAccessToken());
    headers.add(REFRESH_TOKEN, token.getRefreshToken());

    return ResponseEntity.ok().headers(headers).body(null);
  }

}
