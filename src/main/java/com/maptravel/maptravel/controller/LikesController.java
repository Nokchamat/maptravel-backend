package com.maptravel.maptravel.controller;

import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.LikesService;
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
public class LikesController {

  private final LikesService likesService;

  @PostMapping("/plane/{planeId}/likes")
  ResponseEntity<Void> addBookmark(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    likesService.addLikes(user, planeId);

    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/plane/{planeId}/likes")
  ResponseEntity<Void> deleteBookmark(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    likesService.deleteLikes(user, planeId);

    return ResponseEntity.ok(null);
  }

}
