package com.maptravel.maptravel.service;

import static com.maptravel.maptravel.exception.ErrorCode.ALREADY_ADD_BOOKMARK;
import static com.maptravel.maptravel.exception.ErrorCode.ALREADY_DELETE_LIKES;
import static com.maptravel.maptravel.exception.ErrorCode.NOT_FOUND_PLANE;

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

  public void deleteLikes(User user, Long planeId) {
    Likes likes = likesRepository.findByUserIdAndPlaneId(user.getId(), planeId)
        .orElseThrow(() -> new CustomException(ALREADY_DELETE_LIKES));

    likesRepository.delete(likes);
  }
}
