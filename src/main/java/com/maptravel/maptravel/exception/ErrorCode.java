package com.maptravel.maptravel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."),
  ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "해당 이메일로 회원가입 된 유저가 존해합니다."),
  ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임 입니다."),
  MISMATCH_EMAIL_PASSWORD(HttpStatus.BAD_REQUEST, "이메일이나 비밀번호가 일치하지 않습니다."),
  MISMATCH_EMAIL_VERIFY_CODE(HttpStatus.BAD_REQUEST, "이메일 인증코드가 일치하지 않습니다."),

  NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프래시 토큰을 찾을 수 없습니다."),

  NOT_FOUND_PLANE(HttpStatus.BAD_REQUEST, "여행 게시물을 찾을 수 없습니다."),
  
  NOT_FOUND_BOOKMARK(HttpStatus.BAD_REQUEST, "북마크를 찾을 수 없습니다."),

  ALREADY_ADD_BOOKMARK(HttpStatus.BAD_REQUEST, "이미 북마크된 게시물 입니다."),
  
  ALREADY_ADD_FOLLOW(HttpStatus.BAD_REQUEST, "이미 팔로우된 유저 입니다."),

  INVALID_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다."),

  // 공용
  PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다.");

  private final HttpStatus httpStatus;
  private final String detail;
}
