package com.maptravel.maptravel.service;

import com.maptravel.maptravel.domain.dto.UserDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.BookmarkRepository;
import com.maptravel.maptravel.domain.repository.CommentRepository;
import com.maptravel.maptravel.domain.repository.FollowRepository;
import com.maptravel.maptravel.domain.repository.LikesRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final FollowRepository followRepository;

  private final AmazonS3Service amazonS3Service;

  private final SendEmailService sendEmailService;

  private final LikesRepository likesRepository;

  private final BookmarkRepository bookmarkRepository;

  private final PlaneRepository planeRepository;

  private final CommentRepository commentRepository;

  @Transactional
  public void verifyEmail(User user, String code) {

    if (!user.getEmailVerifyCode().equals(code)) {
      throw new CustomException(ErrorCode.MISMATCH_EMAIL_VERIFY_CODE);
    }

    user.setEmailVerify();
    userRepository.save(user);
  }

  public void reSendEmail(User user) {
    if(user.getIsEmailVerify()) {
      throw new CustomException(ErrorCode.ALREADY_VERIFIED_EMAIL);
    }
    sendEmailService.sendMail(user.getEmail(), user.getEmailVerifyCode());
  }

  @Transactional
  public void updateProfileImage(User user, MultipartFile profileImage) {

    user.setProfileImageUrl(
        amazonS3Service.uploadForProfile(profileImage, user.getId()));

    userRepository.save(user);
  }

  @Transactional
  public void updateNickname(User user, String nickname) {

    userRepository.findByNickname(nickname)
        .ifPresent(findUser -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_NICKNAME);
        });

    user.setNickname(nickname);

    userRepository.save(user);
  }

  public UserDto getMyProfile(User user) {

    return UserDto.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .profileImageUrl(user.getProfileImageUrl())
        .isEmailVerify(user.getIsEmailVerify())
        .followerCount(followRepository.countByFollowingId(user.getId()))
        .build();
  }

  public UserDto getProfileByUserId(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    return UserDto.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .profileImageUrl(user.getProfileImageUrl())
        .isEmailVerify(user.getIsEmailVerify())
        .followerCount(followRepository.countByFollowingId(user.getId()))
        .build();
  }

  @Transactional
  public void deleteAccount(User user) {
    planeRepository.findFirstByUserId(user.getId())
        .ifPresent(plane -> {
          throw new CustomException(ErrorCode.PLEASE_REMOVE_PLANE_FIRST);
        });

    commentRepository.deleteAllByUserId(user.getId());
    likesRepository.deleteAllByUserId(user.getId());
    bookmarkRepository.deleteAllByUserId(user.getId());

    userRepository.delete(user);
  }
}
