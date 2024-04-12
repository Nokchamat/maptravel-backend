package com.maptravel.maptravel.controller;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

import com.maptravel.maptravel.domain.form.GoogleSignInForm;
import com.maptravel.maptravel.domain.form.SignInForm;
import com.maptravel.maptravel.domain.form.SignUpForm;
import com.maptravel.maptravel.oauth.domain.Token;
import com.maptravel.maptravel.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
@Slf4j
@Tag(name = "회원 가입 컨트롤러")
public class SignController {

  private final SignService signService;

  @Tag(name = "회원 가입 컨트롤러")
  @Operation(summary = "회원 가입")
  @PostMapping("/signup")
  ResponseEntity<Void> signUp(@RequestBody SignUpForm signUpForm) {

    signService.signUp(signUpForm);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "회원 가입 컨트롤러")
  @Operation(summary = "로컬 로그인")
  @PostMapping("/signin")
  ResponseEntity<Void> signIn(@RequestBody SignInForm signInForm,
      HttpServletRequest request) {
    log.info("[SignInLocal] 로컬 로그인 시도 : " + signInForm.getEmail());

    Token token = signService.signIn(signInForm, request);
    HttpHeaders headers = new HttpHeaders();
    headers.add(ACCESS_TOKEN, token.getAccessToken());
    headers.add(REFRESH_TOKEN, token.getRefreshToken());

    log.info("[SignInLocal] 로컬 로그인 완료 : " + signInForm.getEmail());
    return ResponseEntity.ok().headers(headers).body(null);
  }

  @Tag(name = "회원 가입 컨트롤러")
  @Operation(summary = "구글 로그인")
  @PostMapping("/signin/google")
  ResponseEntity<Void> signInByGoogle(@RequestBody GoogleSignInForm signInForm,
      HttpServletRequest request) {
    log.info("[SignInGoogle] 구글 로그인 시도 : " + signInForm.getEmail());

    Token token = signService.signInByGoogle(signInForm, request);
    HttpHeaders headers = new HttpHeaders();
    headers.add(ACCESS_TOKEN, token.getAccessToken());
    headers.add(REFRESH_TOKEN, token.getRefreshToken());

    log.info("[SignInGoogle] 구글 로그인 완료 : " + signInForm.getEmail());
    return ResponseEntity.ok().headers(headers).body(null);
  }

}
