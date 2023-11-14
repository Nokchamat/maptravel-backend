package com.maptravel.maptravel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."),
  
  NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프래시 토큰을 찾을 수 없습니다."),

  INVALID_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다."),

  // 공용
  PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String detail;
}
