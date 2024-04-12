package com.maptravel.maptravel.controller;

import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.LikesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
@Tag(name = "게시물 좋아요 컨트롤러")
public class LikesController {

  private final LikesService likesService;

  @Tag(name = "게시물 좋아요 컨트롤러")
  @Operation(summary = "게시물 좋아요 누르기")
  @PostMapping("/plane/{planeId}/likes")
  ResponseEntity<Void> addBookmark(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    likesService.addLikes(user, planeId);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "게시물 좋아요 컨트롤러")
  @Operation(summary = "게시물 좋아요 취소")
  @DeleteMapping("/plane/{planeId}/likes")
  ResponseEntity<Void> deleteBookmark(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    likesService.deleteLikes(user, planeId);

    return ResponseEntity.ok(null);
  }

}
