package com.maptravel.maptravel.service;

import static com.maptravel.maptravel.exception.ErrorCode.ALREADY_ADD_BOOKMARK;
import static com.maptravel.maptravel.exception.ErrorCode.NOT_FOUND_BOOKMARK;
import static com.maptravel.maptravel.exception.ErrorCode.NOT_FOUND_PLANE;
import static com.maptravel.maptravel.exception.ErrorCode.PERMISSION_DENIED;

import com.maptravel.maptravel.domain.entity.Likes;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.LikesRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {

  private final LikesRepository likesRepository;

  private final PlaneRepository planeRepository;

  public void addLikes(User user, Long planeId) {

    Plane plane = planeRepository.findById(planeId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_PLANE));

    likesRepository.findByUserIdAndPlaneId(user.getId(), planeId)
        .ifPresent(bookmark -> {
          throw new CustomException(ALREADY_ADD_BOOKMARK);
        });

    likesRepository.save(Likes.builder()
        .plane(plane)
        .user(user)
        .build());
  }

  public void deleteLikes(User user, Long bookmarkId) {

    Likes likes = likesRepository.findById(bookmarkId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_BOOKMARK));

    if (!user.getId().equals(likes.getUser().getId())) {
      throw new CustomException(PERMISSION_DENIED);
    }

    likesRepository.delete(likes);
  }
}
