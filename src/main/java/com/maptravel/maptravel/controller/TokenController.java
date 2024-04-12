package com.maptravel.maptravel.controller;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

import com.maptravel.maptravel.oauth.domain.Token;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import com.maptravel.maptravel.oauth.service.ClientIp;
import com.maptravel.maptravel.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/token")
@Tag(name = "토큰 컨트롤러")
public class TokenController {

  private final JwtTokenProvider jwtTokenProvider;
  private final TokenService tokenService;

  @Tag(name = "토큰 컨트롤러")
  @Operation(summary = "토큰 갱신",
      description = "Refresh Token을 헤더에 넣어서 요청 시 새로운 Refresh Token과 Access Token 리턴")
  @GetMapping("/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
    String token = request.getHeader(REFRESH_TOKEN);

    if (token != null && jwtTokenProvider.isTokenValid(token)) {

      Token newToken = tokenService.refreshToken(token, ClientIp.getClientIp(request));

      response.addHeader(ACCESS_TOKEN, newToken.getAccessToken());
      response.addHeader(REFRESH_TOKEN, newToken.getRefreshToken());
      response.setContentType("application/json;charset=UTF-8");
    }

  }
}