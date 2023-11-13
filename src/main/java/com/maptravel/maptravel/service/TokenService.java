package com.maptravel.maptravel.service;

import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import com.maptravel.maptravel.oauth.domain.RefreshToken;
import com.maptravel.maptravel.oauth.domain.RefreshTokenRepository;
import com.maptravel.maptravel.oauth.domain.Token;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional
  public Token refreshToken(String token, String currentIp) {

    RefreshToken refreshToken = refreshTokenRepository.findById(token)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));

    if (!refreshToken.getIp().equals(currentIp)) {
      refreshTokenRepository.delete(refreshToken);

      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

    Token newToken = jwtTokenProvider.generateToken(jwtTokenProvider.getEmail(token));
    refreshToken.setRefreshToken(newToken.getRefreshToken());

    return newToken;
  }

}
