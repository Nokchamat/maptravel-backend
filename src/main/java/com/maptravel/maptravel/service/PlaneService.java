package com.maptravel.maptravel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maptravel.maptravel.domain.dto.PlaceDto;
import com.maptravel.maptravel.domain.dto.PlaneDto;
import com.maptravel.maptravel.domain.dto.PlaneListDto;
import com.maptravel.maptravel.domain.entity.Place;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.form.CreatePlaceForm;
import com.maptravel.maptravel.domain.form.CreatePlaneForm;
import com.maptravel.maptravel.domain.repository.BookmarkRepository;
import com.maptravel.maptravel.domain.repository.LikesRepository;
import com.maptravel.maptravel.domain.repository.PlaceRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaneService {

  private final PlaneRepository planeRepository;

  private final PlaceRepository placeRepository;

  private final BookmarkRepository bookmarkRepository;

  private final LikesRepository likesRepository;

  private final UserRepository userRepository;

  private final AmazonS3Service amazonS3Service;

  private final ObjectMapper objectMapper;

  @Transactional
  public void createPlane(User user, CreatePlaneForm createPlaneForm) {
    if (!user.getIsEmailVerify()) {
      throw new CustomException(ErrorCode.PLEASE_VERIFY_EMAIL);
    }

    Plane plane = planeRepository.save(Plane.builder()
        .subject(createPlaneForm.getSubject())
        .content(createPlaneForm.getContent())
        .country(createPlaneForm.getCountry())
        .city(createPlaneForm.getCity())
        .viewCount(0L)
        .user(user)
        .build());
    String uuid = UUID.randomUUID().toString();
    plane.setThumbnailUrl(amazonS3Service.uploadForThumbnail(
        createPlaneForm.getThumbnail(), plane.getId(), uuid));

    for (int i = 0; i < createPlaneForm.getCreatePlaceFormList().size(); i++) {
      CreatePlaceForm createPlaceForm =
          createPlaneForm.getCreatePlaceFormList().get(i);

      placeRepository.save(Place.builder()
          .subject(createPlaceForm.getSubject())
          .content(createPlaceForm.getContent())
          .address(createPlaceForm.getAddress())
          .plane(plane)
          .latitude(createPlaceForm.getLatitude())
          .longitude(createPlaceForm.getLongitude())
          .pictureListUrl(
              amazonS3Service.uploadForPictureList(
                  createPlaceForm.getPictureList(), i, plane.getId(), uuid)
          ).build());
    }
  }

  public Page<PlaneListDto> getPlaneList(User user, Pageable pageable) {
    return planeRepository.findAll(pageable)
        .map(plane -> toPlaneListDto(plane, user));
  }

  @Transactional
  public PlaneDto getPlaneDetail(User user, Long planeId) {
    Plane plane = planeRepository.findById(planeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PLANE));
    plane.addViewCount();

    List<PlaceDto> placeDtoList = placeRepository.findAllByPlaneId(planeId)
        .stream().map(place -> {

          PlaceDto placeDto = PlaceDto.builder()
              .id(place.getId())
              .subject(place.getSubject())
              .content(place.getContent())
              .address(place.getAddress())
              .latitude(place.getLatitude())
              .longitude(place.getLongitude())
              .build();

          try {
            placeDto.setPictureUrlArray(
                objectMapper.readValue(place.getPictureListUrl(),
                    String[].class));
          } catch (JsonProcessingException exception) {
            exception.printStackTrace();
          }

          return placeDto;
        }).collect(Collectors.toList());
    PlaneDto planeDto = PlaneDto.fromEntity(plane, placeDtoList);

    if (user != null) {
      bookmarkRepository.findByUserIdAndPlaneId(user.getId(), planeId)
          .ifPresent(bookmark -> planeDto.setBookmark(true));
      likesRepository.findByUserIdAndPlaneId(user.getId(), planeId)
          .ifPresent(bookmark -> planeDto.setLikes(true));
    }

    return planeDto;
  }

  @Transactional
  public void deletePlane(User user, Long planeId) {
    Plane plane = planeRepository.findById(planeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PLANE));

    if (!plane.getUser().getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.PERMISSION_DENIED);
    }
    amazonS3Service.deleteUploadFile(plane.getThumbnailUrl());

    placeRepository.findAllByPlaneId(planeId).forEach(place -> {
      try {
        amazonS3Service.deleteUploadFileArray(
            objectMapper.readValue(place.getPictureListUrl(), String[].class));

        placeRepository.delete(place);
      } catch (JsonProcessingException exception) {
        exception.printStackTrace();
      }
    });

    likesRepository.deleteAllByPlaneId(planeId);
    bookmarkRepository.deleteAllByPlaneId(planeId);

    planeRepository.delete(plane);
  }

  public Page<PlaneListDto> getPlaneListByCountryOrCity(User user, String country, String city,
      Pageable pageable) {
    return planeRepository.findAllByCountryOrCity(country, city, pageable)
        .map(plane -> toPlaneListDto(plane, user));
  }

  public Page<PlaneListDto> getPlaneListByNickname(User user, String nickname, Pageable pageable) {
    User writer = userRepository.findByNickname(nickname)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    return getPlaneListDto(user, pageable, writer);
  }

  public Page<PlaneListDto> getPlaneListByUserId(User user, Long userId, Pageable pageable) {
    User writer = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    return getPlaneListDto(user, pageable, writer);
  }

  public Page<PlaneListDto> getMyPlaneList(User user, Pageable pageable) {
    return planeRepository.findAllByUserId(user.getId(), pageable)
        .map(plane -> toPlaneListDto(plane, user));
  }


  private Page<PlaneListDto> getPlaneListDto(User user, Pageable pageable, User writer) {
    return planeRepository.findAllByUserId(writer.getId(), pageable)
        .map(plane -> toPlaneListDto(plane, user));
  }

  private PlaneListDto toPlaneListDto(Plane plane, User user) {
    PlaneListDto planeListDto = PlaneListDto.fromEntity(plane);
    planeListDto.setLikesCount(likesRepository.countByPlaneId(plane.getId()));

    if (user != null) {
      bookmarkRepository.findByUserIdAndPlaneId(user.getId(), plane.getId())
          .ifPresent(bookmark -> planeListDto.setBookmark(true));
      likesRepository.findByUserIdAndPlaneId(user.getId(), plane.getId())
          .ifPresent(bookmark -> planeListDto.setLikes(true));
    }

    return planeListDto;
  }
}
