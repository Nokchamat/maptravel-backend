package com.maptravel.maptravel.controller;


import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.form.CreatePlaneForm;
import com.maptravel.maptravel.service.PlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/place")
public class PlaneController {

  private final PlaneService planeService;

  @PostMapping
  ResponseEntity<Void> createPlane(@AuthenticationPrincipal User user,
      @ModelAttribute CreatePlaneForm createPlaneForm) {

    planeService.createPlane(user, createPlaneForm);

    return ResponseEntity.ok(null);
  }

}
