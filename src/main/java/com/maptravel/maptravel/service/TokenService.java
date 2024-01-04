package com.maptravel.maptravel.service;

import com.amazonaws.services.kms.model.InvalidGrantTokenException;
import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import com.maptravel.maptravel.oauth.domain.RefreshToken;
import com.maptravel.maptravel.oauth.domain.RefreshTokenRepository;
import com.maptravel.maptravel.oauth.domain.Token;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional(noRollbackFor = InvalidGrantTokenException.class)
  public Token refreshToken(String token, String currentIp) {
    RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
        .orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));

    if (!refreshToken.getIp().equals(currentIp)) {
      refreshTokenRepository.delete(refreshToken);

      throw new InvalidGrantTokenException("접속 시도한 IP가 다릅니다.");
    }

    Token newToken = jwtTokenProvider.generateToken(
        jwtTokenProvider.getEmail(token));
    refreshToken.setRefreshToken(newToken.getRefreshToken());

    return newToken;
  }

  @Scheduled(cron = "0 0 3 * * *")
  public void removeRefreshToken() {
    refreshTokenRepository.deleteAllByCreatedAtLessThanEqual(
        LocalDateTime.now().minusDays(14));
  }

}
