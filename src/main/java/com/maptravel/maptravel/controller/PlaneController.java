package com.maptravel.maptravel.controller;


import com.maptravel.maptravel.domain.dto.PlaneDto;
import com.maptravel.maptravel.domain.dto.PlaneListDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.form.CreatePlaneForm;
import com.maptravel.maptravel.service.PlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/plane")
public class PlaneController {

  private final PlaneService planeService;

  @PostMapping
  ResponseEntity<Void> createPlane(@AuthenticationPrincipal User user,
      @ModelAttribute CreatePlaneForm createPlaneForm) {

    planeService.createPlane(user, createPlaneForm);

    return ResponseEntity.ok(null);
  }

  @GetMapping
  ResponseEntity<Page<PlaneListDto>> getPlaneList(@AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(planeService.getPlaneList(user, pageable));
  }

  @GetMapping("/location")
  ResponseEntity<Page<PlaneListDto>> getPlaneListByLocation(@AuthenticationPrincipal User user,
      Pageable pageable, @RequestParam String country, @RequestParam String city) {

    return ResponseEntity.ok(planeService.getPlaneListByCountryOrCity(user, country, city, pageable));
  }

  @GetMapping("/nickname")
  ResponseEntity<Page<PlaneListDto>> getPlaneListByNickname(@AuthenticationPrincipal User user,
      Pageable pageable, @RequestParam String nickname) {

    return ResponseEntity.ok(planeService.getPlaneListByNickname(user, nickname, pageable));
  }

  @GetMapping("/user/{userId}")
  ResponseEntity<Page<PlaneListDto>> getPlaneListByUserId(@AuthenticationPrincipal User user,
      @PathVariable Long userId, Pageable pageable) {

    return ResponseEntity.ok(planeService.getPlaneListByUserId(user, userId, pageable));
  }

  @GetMapping("/myplane")
  ResponseEntity<Page<PlaneListDto>> getMyPlane(@AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(planeService.getMyPlaneList(user, pageable));
  }

  @GetMapping("/{planeId}")
  ResponseEntity<PlaneDto> getPlaneDetail(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    return ResponseEntity.ok(planeService.getPlaneDetail(user, planeId));
  }

  @DeleteMapping("/{planeId}")
  ResponseEntity<Void> deletePlane(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    planeService.deletePlane(user, planeId);
    return ResponseEntity.ok(null);
  }

}
