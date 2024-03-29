package com.maptravel.maptravel.oauth.jwt;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("[JwtAuthenticationFilter] 진입");
    Optional<String> token = JwtTokenProvider.resolveToken(request);

    if (token.isEmpty() || token.get().equals("")) {
      filterChain.doFilter(request, response);
      return;
    }

    log.info("[JwtAuthenticationFilter]  TokenValidate : " + token.get());
    String email = jwtTokenProvider.getEmail(token.get());
    log.info("[JwtAuthenticationFilter] TokenVerify 시작 Email : " + email);

    if (!jwtTokenProvider.isTokenValid(token.get())) {
      log.info("[JwtAuthenticationFilter] TokenVerify 실패 Email : " + email);
    }

    log.info("[JwtAuthenticationFilter] TokenVerify 완료 Email : " + email);
    SecurityContextHolder.getContext().setAuthentication(
        jwtTokenProvider.getAuthentication(token.get()));
    filterChain.doFilter(request, response);
  }

}
