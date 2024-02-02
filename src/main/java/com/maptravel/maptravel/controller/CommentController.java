package com.maptravel.maptravel.controller;

import com.maptravel.maptravel.domain.dto.CommentDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/plane/{planeId}/comment")
  ResponseEntity<Void> createComment(@AuthenticationPrincipal User user,
      @PathVariable Long planeId,
      @RequestParam String comment) {

    commentService.createComment(user, planeId, comment);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/plane/{planeId}/comment")
  ResponseEntity<Page<CommentDto>> getComment(@AuthenticationPrincipal User user,
      @PathVariable Long planeId, Pageable pageable) {

    return ResponseEntity.ok(commentService.getComment(user, planeId, pageable));
  }

  @DeleteMapping("/comment/{commentId}")
  ResponseEntity<Void> deleteComment(@AuthenticationPrincipal User user,
      @PathVariable Long commentId) {

    commentService.deleteComment(user, commentId);

    return ResponseEntity.ok(null);
  }

}
