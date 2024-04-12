package com.maptravel.maptravel.controller;


import com.maptravel.maptravel.domain.dto.PlaneDto;
import com.maptravel.maptravel.domain.dto.PlaneListDto;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.form.CreatePlaneForm;
import com.maptravel.maptravel.service.PlaneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "여행 게시물 컨트롤러")
public class PlaneController {

  private final PlaneService planeService;

  @Tag(name = "여행 게시물 컨트롤러")
  @Operation(summary = "여행 게시물 작성")
  @PostMapping
  ResponseEntity<Void> createPlane(@AuthenticationPrincipal User user,
      @ModelAttribute CreatePlaneForm createPlaneForm) {

    planeService.createPlane(user, createPlaneForm);

    return ResponseEntity.ok(null);
  }

  @Tag(name = "여행 게시물 컨트롤러")
  @Operation(summary = "여행 게시물 리스트 조회", description = "여행 게시물을 Pageable로 가져옵니다.")
  @GetMapping
  ResponseEntity<Page<PlaneListDto>> getPlaneList(@AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(planeService.getPlaneList(user, pageable));
  }

  @Tag(name = "여행 게시물 컨트롤러")
  @Operation(summary = "국가 및 도시명 검색 - 여행 게시물 리스트 조회",
      description = "국가 및 도시명으로 검색하여 그에 맞는 여행 게시물을 Pageable로 가져옵니다.")
  @GetMapping("/location")
  ResponseEntity<Page<PlaneListDto>> getPlaneListByLocation(@AuthenticationPrincipal User user,
      Pageable pageable, @RequestParam String country, @RequestParam String city) {

    return ResponseEntity.ok(
        planeService.getPlaneListByCountryOrCity(user, country, city, pageable));
  }

  @Tag(name = "여행 게시물 컨트롤러")
  @Operation(summary = "닉네임 검색 - 여행 게시물 리스트 조회",
      description = "유저 닉네임으로 검색하여 여행 게시물을 Pageable로 가져옵니다.")
  @GetMapping("/nickname")
  ResponseEntity<Page<PlaneListDto>> getPlaneListByNickname(@AuthenticationPrincipal User user,
      Pageable pageable, @RequestParam String nickname) {

    return ResponseEntity.ok(planeService.getPlaneListByNickname(user, nickname, pageable));
  }

  @Tag(name = "여행 게시물 컨트롤러")
  @Operation(summary = "유저 ID 검색 - 여행 게시물 리스트 조회",
      description = "유저 ID로 검색하여 여행 게시물을 Pageable로 가져옵니다.")
  @GetMapping("/user/{userId}")
  ResponseEntity<Page<PlaneListDto>> getPlaneListByUserId(@AuthenticationPrincipal User user,
      @PathVariable Long userId, Pageable pageable) {

    return ResponseEntity.ok(planeService.getPlaneListByUserId(user, userId, pageable));
  }

  @Tag(name = "여행 게시물 컨트롤러")
  @Operation(summary = "자신이 작성한 여행 게시물 조회")
  @GetMapping("/myplane")
  ResponseEntity<Page<PlaneListDto>> getMyPlaneList(@AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(planeService.getMyPlaneList(user, pageable));
  }

  @Tag(name = "여행 게시물 컨트롤러")
  @Operation(summary = "여행 게시물 상세 조회")
  @GetMapping("/{planeId}")
  ResponseEntity<PlaneDto> getPlaneDetail(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    return ResponseEntity.ok(planeService.getPlaneDetail(user, planeId));
  }

  @Tag(name = "여행 게시물 컨트롤러")
  @Operation(summary = "여행 게시물 삭제", description = "작성한 본인만 삭제가 가능합니다.")
  @DeleteMapping("/{planeId}")
  ResponseEntity<Void> deletePlane(@AuthenticationPrincipal User user,
      @PathVariable Long planeId) {

    planeService.deletePlane(user, planeId);
    return ResponseEntity.ok(null);
  }

}
