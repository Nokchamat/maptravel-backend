package com.maptravel.maptravel.service;

import static com.maptravel.maptravel.exception.ErrorCode.ALREADY_ADD_FOLLOW;
import static com.maptravel.maptravel.exception.ErrorCode.NOT_FOUND_BOOKMARK;
import static com.maptravel.maptravel.exception.ErrorCode.NOT_FOUND_USER;
import static com.maptravel.maptravel.exception.ErrorCode.PERMISSION_DENIED;

import com.maptravel.maptravel.domain.dto.FollowDto;
import com.maptravel.maptravel.domain.entity.Follow;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.FollowRepository;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final FollowRepository followRepository;

  private final UserRepository userRepository;

  @Transactional
  public void addFollow(User user, Long followingId) {
    User following = userRepository.findById(followingId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    followRepository.findByFollowerIdAndFollowingId(user.getId(), followingId)
        .ifPresent(follow -> {
          throw new CustomException(ALREADY_ADD_FOLLOW);
        });

    followRepository.save(Follow.builder()
        .following(following)
        .follower(user)
        .build());
  }


  public Page<FollowDto> getFollow(User user, Pageable pageable) {

    return followRepository.findByFollowerId(user.getId(), pageable)
        .map(follow -> {
          User following = follow.getFollowing();

          return FollowDto.builder()
              .id(follow.getId())
              .userProfileImageUrl(following.getProfileImageUrl())
              .userNickname(following.getNickname())
              .createdAt(follow.getCreatedAt())
              .build();
        });
  }

  @Transactional
  public void deleteFollow(User user, Long followId) {

    Follow follow = followRepository.findById(followId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_BOOKMARK));

    if (!user.getId().equals(follow.getFollower().getId())) {
      throw new CustomException(PERMISSION_DENIED);
    }

    followRepository.delete(follow);
  }
}
