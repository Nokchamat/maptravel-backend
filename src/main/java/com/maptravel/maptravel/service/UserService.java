package com.maptravel.maptravel.service;

import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public void verifyEmail(User user, String code) {

    if (!user.getEmailVerifyCode().equals(code)) {
      throw new CustomException(ErrorCode.MISMATCH_EMAIL_VERIFY_CODE);
    }

    user.setEmailVerify();
    userRepository.save(user);
  }
}
