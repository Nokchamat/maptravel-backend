package com.maptravel.maptravel.controller;

import com.maptravel.maptravel.domain.dto.BookmarkDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.BookmarkService;
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
public class BookmarkController {

  private final BookmarkService bookmarkService;

  @PostMapping("/plane/{planeId}/bookmark")
  ResponseEntity<Void> addBookmark(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    bookmarkService.addBookmark(user, planeId);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/plane/bookmark")
  ResponseEntity<Page<BookmarkDto>> getBookmark(@AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(bookmarkService.getBookmark(user, pageable));
  }

  @DeleteMapping("/plane/bookmark/{bookmarkId}")
  ResponseEntity<Void> deleteBookmark(@AuthenticationPrincipal User user,
      @PathVariable Long bookmarkId) {

    bookmarkService.deleteBookmark(user, bookmarkId);

    return ResponseEntity.ok(null);
  }

}
