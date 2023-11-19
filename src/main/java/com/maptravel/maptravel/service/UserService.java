package com.maptravel.maptravel.service;

import com.maptravel.maptravel.domain.entity.User;
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

  private final AmazonS3Service amazonS3Service;

  @Transactional
  public void verifyEmail(User user, String code) {

    if (!user.getEmailVerifyCode().equals(code)) {
      throw new CustomException(ErrorCode.MISMATCH_EMAIL_VERIFY_CODE);
    }

    user.setEmailVerify();
    userRepository.save(user);
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
}
