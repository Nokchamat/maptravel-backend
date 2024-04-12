package com.maptravel.maptravel.controller;

import com.maptravel.maptravel.domain.dto.FollowDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
@Tag(name = "유저 팔로우 컨트롤러")
public class FollowController {

  private final FollowService followService;

  @Tag(name = "유저 팔로우 컨트롤러")
  @Operation(summary = "팔로우 추가")
  @PostMapping("/user/{userId}/follow")
  ResponseEntity<Void> addFollow(@AuthenticationPrincipal User user,
      @PathVariable Long userId) {

    followService.addFollow(user, userId);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "유저 팔로우 컨트롤러")
  @Operation(summary = "팔로우 한 유저 조회")
  @GetMapping("/user/follow")
  ResponseEntity<Page<FollowDto>> getFollow(@AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(followService.getFollow(user, pageable));
  }

  @Tag(name = "유저 팔로우 컨트롤러")
  @Operation(summary = "팔로우 취소")
  @DeleteMapping("/user/follow/{followId}")
  ResponseEntity<Void> deleteFollow(@AuthenticationPrincipal User user,
      @PathVariable Long followId) {

    followService.deleteFollow(user, followId);

    return ResponseEntity.ok(null);
  }

}
