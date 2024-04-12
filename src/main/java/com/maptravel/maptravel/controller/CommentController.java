package com.maptravel.maptravel.controller;

import com.maptravel.maptravel.domain.dto.CommentDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
@Tag(name = "게시물 댓글 컨트롤러")
public class CommentController {

  private final CommentService commentService;

  @Tag(name = "게시물 댓글 컨트롤러")
  @Operation(summary = "댓글 추가")
  @PostMapping("/plane/{planeId}/comment")
  ResponseEntity<Void> createComment(@AuthenticationPrincipal User user,
      @PathVariable Long planeId,
      @RequestParam String comment) {

    commentService.createComment(user, planeId, comment);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "게시물 댓글 컨트롤러")
  @Operation(summary = "댓글 조회")
  @GetMapping("/plane/{planeId}/comment")
  ResponseEntity<Page<CommentDto>> getComment(@AuthenticationPrincipal User user,
      @PathVariable Long planeId, Pageable pageable) {

    return ResponseEntity.ok(commentService.getComment(user, planeId, pageable));
  }

  @Tag(name = "게시물 댓글 컨트롤러")
  @Operation(summary = "댓글 삭제", description = "댓글 삭제는 댓글 작성한 본인만 삭제가 가능합니다.")
  @DeleteMapping("/comment/{commentId}")
  ResponseEntity<Void> deleteComment(@AuthenticationPrincipal User user,
      @PathVariable Long commentId) {

    commentService.deleteComment(user, commentId);

    return ResponseEntity.ok(null);
  }

}

