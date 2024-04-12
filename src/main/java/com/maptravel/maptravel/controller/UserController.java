package com.maptravel.maptravel.controller;


import com.maptravel.maptravel.domain.dto.UserDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
@Tag(name = "유저 컨트롤러")
public class UserController {

  private final UserService userService;

  @Tag(name = "유저 컨트롤러")
  @Operation(summary = "이메일 인증")
  @PostMapping("/verify")
  ResponseEntity<Void> verifyEmail(@AuthenticationPrincipal User user,
      @RequestParam String code) {
    userService.verifyEmail(user, code);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "유저 컨트롤러")
  @Operation(summary = "이메일로 코드 재발송")
  @GetMapping("/resend-email")
  ResponseEntity<Void> reSendEmail(@AuthenticationPrincipal User user) {
    userService.reSendEmail(user);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "유저 컨트롤러")
  @Operation(summary = "내 프로필 조회")
  @GetMapping("/myprofile")
  ResponseEntity<UserDto> getMyProfile(@AuthenticationPrincipal User user) {

    return ResponseEntity.ok(userService.getMyProfile(user));
  }

  @Tag(name = "유저 컨트롤러")
  @Operation(summary = "유저 프로필 조회")
  @GetMapping("/{userId}/profile")
  ResponseEntity<UserDto> getProfileByUserId(@PathVariable Long userId) {

    return ResponseEntity.ok(userService.getProfileByUserId(userId));
  }

  @Tag(name = "유저 컨트롤러")
  @Operation(summary = "프로필 사진 수정")
  @PutMapping("/profileimage")
  ResponseEntity<Void> updateProfileImage(@AuthenticationPrincipal User user,
      @ModelAttribute MultipartFile profileImage) {
    userService.updateProfileImage(user, profileImage);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "유저 컨트롤러")
  @Operation(summary = "닉네임 수정")
  @PutMapping("/nickname")
  ResponseEntity<Void> updateNickname(@AuthenticationPrincipal User user,
      @ModelAttribute(name = "nickname") String nickname) {
    userService.updateNickname(user, nickname);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "유저 컨트롤러")
  @Operation(summary = "회원 탈퇴")
  @DeleteMapping
  ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal User user) {
    userService.deleteAccount(user);

    return ResponseEntity.ok(null);
  }

}
