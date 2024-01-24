package com.maptravel.maptravel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."),
  ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "해당 이메일로 회원가입 된 유저가 존재합니다."),
  ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
  MISMATCH_EMAIL_PASSWORD(HttpStatus.BAD_REQUEST, "이메일이나 비밀번호가 일치하지 않습니다."),
  MISMATCHED_PLATFORM(HttpStatus.BAD_REQUEST, "다른 플랫폼으로 회원가입된 계정이 있습니다."),
  MISMATCH_EMAIL_VERIFY_CODE(HttpStatus.BAD_REQUEST, "이메일 인증코드가 일치하지 않습니다."),

  NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프래시 토큰을 찾을 수 없습니다."),

  NOT_FOUND_PLANE(HttpStatus.BAD_REQUEST, "여행 게시물을 찾을 수 없습니다."),
  PLEASE_REMOVE_PLANE_FIRST(HttpStatus.BAD_REQUEST, "계정 탈퇴 전 작성한 여행을 삭제해주세요."),

  PLEASE_VERIFY_EMAIL(HttpStatus.BAD_REQUEST, "이메일 인증을 완료해주세요."),

  NOT_FOUND_BOOKMARK(HttpStatus.BAD_REQUEST, "북마크를 찾을 수 없습니다."),
  ALREADY_DELETE_BOOKMARK(HttpStatus.BAD_REQUEST, "이미 삭제된 북마크입니다."),
  ALREADY_VERIFIED_EMAIL(HttpStatus.BAD_REQUEST, "이미 인증된 이메일입니다."),

  ALREADY_DELETE_LIKES(HttpStatus.BAD_REQUEST, "이미 취소된 좋아요입니다."),

  ALREADY_ADD_BOOKMARK(HttpStatus.BAD_REQUEST, "이미 북마크된 게시물입니다."),
  
  ALREADY_ADD_FOLLOW(HttpStatus.BAD_REQUEST, "이미 팔로우된 유저입니다."),

  INVALID_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다."),

  ALREADY_DELETE_COMMENT(HttpStatus.BAD_REQUEST, "이미 삭제된 댓글입니다."),

  // 공용
  PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다.");

  private final HttpStatus httpStatus;
  private final String detail;
}
