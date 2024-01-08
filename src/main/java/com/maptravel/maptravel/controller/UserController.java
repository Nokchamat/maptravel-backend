package com.maptravel.maptravel.controller;


import com.maptravel.maptravel.domain.dto.UserDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.UserService;
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
public class UserController {

  private final UserService userService;

  @PostMapping("/verify")
  ResponseEntity<Void> verifyEmail(@AuthenticationPrincipal User user,
      @RequestParam String code) {
    userService.verifyEmail(user, code);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/resend-email")
  ResponseEntity<Void> reSendEmail(@AuthenticationPrincipal User user) {
    userService.reSendEmail(user);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/myprofile")
  ResponseEntity<UserDto> getMyProfile(@AuthenticationPrincipal User user) {

    return ResponseEntity.ok(userService.getMyProfile(user));
  }

  @GetMapping("/{userId}/profile")
  ResponseEntity<UserDto> getProfileByUserId(@PathVariable Long userId) {

    return ResponseEntity.ok(userService.getProfileByUserId(userId));
  }

  @PutMapping("/profileimage")
  ResponseEntity<Void> updateProfileImage(@AuthenticationPrincipal User user,
      @ModelAttribute MultipartFile profileImage) {
    userService.updateProfileImage(user, profileImage);

    return ResponseEntity.ok(null);
  }

  @PutMapping("/nickname")
  ResponseEntity<Void> updateNickname(@AuthenticationPrincipal User user,
      @ModelAttribute(name = "nickname") String nickname) {
    userService.updateNickname(user, nickname);

    return ResponseEntity.ok(null);
  }

  @DeleteMapping()
  ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal User user) {
    userService.deleteAccount(user);

    return ResponseEntity.ok(null);
  }

}
