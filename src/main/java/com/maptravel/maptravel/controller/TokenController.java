package com.maptravel.maptravel.controller;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import com.maptravel.maptravel.oauth.domain.Token;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import com.maptravel.maptravel.oauth.service.ClientIp;
import com.maptravel.maptravel.service.TokenService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class TokenController {

  private final JwtTokenProvider jwtTokenProvider;
  private final TokenService tokenService;

  @GetMapping("/token/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
    String token = request.getHeader(REFRESH_TOKEN);

    if (token != null && jwtTokenProvider.isTokenValid(token)) {

      Token newToken = tokenService.refreshToken(token, ClientIp.getClientIp(request));

      response.addHeader(ACCESS_TOKEN, newToken.getAccessToken());
      response.addHeader(REFRESH_TOKEN, newToken.getRefreshToken());
      response.setContentType("application/json;charset=UTF-8");

      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

  }
}