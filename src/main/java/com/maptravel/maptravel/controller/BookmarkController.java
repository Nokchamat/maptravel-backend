package com.maptravel.maptravel.controller;

import com.maptravel.maptravel.domain.dto.BookmarkDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.BookmarkService;
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
@Tag(name = "게시물 북마크 컨트롤러")
public class BookmarkController {

  private final BookmarkService bookmarkService;

  @Tag(name = "게시물 북마크 컨트롤러")
  @Operation(summary = "북마크 추가")
  @PostMapping("/plane/{planeId}/bookmark")
  ResponseEntity<Void> addBookmark(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    bookmarkService.addBookmark(user, planeId);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "게시물 북마크 컨트롤러")
  @Operation(summary = "북마크 조회")
  @GetMapping("/plane/bookmark")
  ResponseEntity<Page<BookmarkDto>> getBookmark(@AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(bookmarkService.getBookmark(user, pageable));
  }

  @Tag(name = "게시물 북마크 컨트롤러")
  @Operation(summary = "북마크 삭제")
  @DeleteMapping("/plane/{planeId}/bookmark")
  ResponseEntity<Void> deleteBookmark(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    bookmarkService.deleteBookmark(user, planeId);

    return ResponseEntity.ok(null);
  }

}
