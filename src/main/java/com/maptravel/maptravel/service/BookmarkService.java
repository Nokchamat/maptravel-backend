package com.maptravel.maptravel.service;

import static com.maptravel.maptravel.exception.ErrorCode.ALREADY_ADD_BOOKMARK;
import static com.maptravel.maptravel.exception.ErrorCode.ALREADY_DELETE_BOOKMARK;
import static com.maptravel.maptravel.exception.ErrorCode.NOT_FOUND_PLANE;

import com.maptravel.maptravel.domain.dto.BookmarkDto;
import com.maptravel.maptravel.domain.entity.Bookmark;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.BookmarkRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;

  private final PlaneRepository planeRepository;

  @Transactional
  public void addBookmark(User user, Long planeId) {

    Plane plane = planeRepository.findById(planeId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_PLANE));

    bookmarkRepository.findByUserIdAndPlaneId(user.getId(), planeId)
        .ifPresent(bookmark -> {
          throw new CustomException(ALREADY_ADD_BOOKMARK);
        });

    bookmarkRepository.save(Bookmark.builder()
        .plane(plane)
        .user(user)
        .build());
  }

  public Page<BookmarkDto> getBookmark(User user, Pageable pageable) {
    return bookmarkRepository.findAllByUserId(user.getId(), pageable)
        .map(bookmark -> {
          Plane plane = bookmark.getPlane();

          return BookmarkDto.builder()
              .id(bookmark.getId())
              .planeId(plane.getId())
              .subject(plane.getSubject())
              .content(plane.getContent())
              .country(plane.getCountry())
              .city(plane.getCity())
              .thumbnailUrl(plane.getThumbnailUrl())
              .build();
        });
  }

  @Transactional
  public void deleteBookmark(User user, Long planeId) {
    Bookmark bookmark = bookmarkRepository.findByUserIdAndPlaneId(user.getId(), planeId)
        .orElseThrow(() -> new CustomException(ALREADY_DELETE_BOOKMARK));

    bookmarkRepository.delete(bookmark);
  }
}
