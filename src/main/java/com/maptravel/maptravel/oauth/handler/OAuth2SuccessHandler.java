package com.maptravel.maptravel.oauth.handler;


import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maptravel.maptravel.oauth.domain.PrincipalDetails;
import com.maptravel.maptravel.oauth.domain.RefreshToken;
import com.maptravel.maptravel.oauth.domain.RefreshTokenRepository;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import com.maptravel.maptravel.oauth.domain.Token;
import com.maptravel.maptravel.oauth.service.ClientIp;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication)
      throws IOException, ServletException {
    PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
    String email = principal.getUser().getEmail();
    log.info("[onAuthenticationSuccess] 진입 Email : {}", email);

    Token token = jwtTokenProvider.generateToken(email);

    refreshTokenRepository.save(
        new RefreshToken(token.getRefreshToken(), ClientIp.getClientIp(request)));

    log.info("[onAuthenticationSuccess] 토큰 생성 완료 Email : {}, token : {}", email, token);
    log.info("{}", token);
    writeTokenResponse(response, token);
  }

  private void writeTokenResponse(HttpServletResponse response, Token token)
      throws IOException {
    response.setContentType("text/html;charset=UTF-8");

    response.addHeader(ACCESS_TOKEN, token.getAccessToken());
    response.addHeader(REFRESH_TOKEN, token.getRefreshToken());
    response.setContentType("application/json;charset=UTF-8");

    var writer = response.getWriter();
    writer.println(objectMapper.writeValueAsString(token));
    writer.flush();
  }
}