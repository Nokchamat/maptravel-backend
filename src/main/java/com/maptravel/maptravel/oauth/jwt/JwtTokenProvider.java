package com.maptravel.maptravel.oauth.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import com.maptravel.maptravel.oauth.domain.Token;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  @Value("${spring.jwt.secret-key}")
  private String secretKey;

  @Value("${spring.jwt.access.expiration}")
  private Long accessTokenExpireTime;

  @Value("${spring.jwt.refresh.expiration}")
  private Long refreshTokenExpireTime;

  private final UserRepository userRepository;

  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

  private static final String EMAIL_CLAIM = "email";
  public static final String PREFIX = "Bearer ";

  public Token generateToken(String email) {
    Date now = new Date();

    return new Token(
        PREFIX + JWT.create()
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + accessTokenExpireTime))
            .withIssuedAt(new Date(now.getTime()))
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC256(secretKey)),
        PREFIX + JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + refreshTokenExpireTime))
            .withIssuedAt(new Date(now.getTime()))
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC256(secretKey)));
  }

  public boolean isTokenValid(String token) {

    log.info("[Token Valid] Token 검증 시작 : " + token);

    try {
      token = token.substring(PREFIX.length());

      log.info("[Token Valid] Token 검증 완료 : " + token);
      return true;
    } catch (SignatureVerificationException e) {
      log.error("[Signature verification failed] : " + e.getMessage());
    } catch (TokenExpiredException e) {
      log.error("[Token expired] : " + e.getMessage());
    } catch (Exception e) {
      log.error("[Invalid token] : " + e.getMessage());
    }

    log.error("[Token Valid] Token 검증 실패 : " + token);
    return false;
  }

  public Authentication getAuthentication(String token) {

    String email = getEmail(token);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

    return new UsernamePasswordAuthenticationToken(user, "", authorities);
  }

  public String getEmail(String token) {
    token = token.substring(PREFIX.length());
    DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);

    return decodedJWT.getClaim(EMAIL_CLAIM).asString();
  }

  public static Optional<String> resolveToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_SUBJECT));
  }

}
