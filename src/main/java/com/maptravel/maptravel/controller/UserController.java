package com.maptravel.maptravel.controller;


import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/verify")
  ResponseEntity<Void> verifyEmail(@AuthenticationPrincipal User user, @RequestParam String code) {

    userService.verifyEmail(user, code);

    return ResponseEntity.ok(null);
  }

}
