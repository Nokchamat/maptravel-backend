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
import com.maptravel.maptravel.domain.repository.FollowRepository;
import com.maptravel.maptravel.domain.repository.LikesRepository;
import com.maptravel.maptravel.domain.repository.PlaceRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import java.util.List;
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

  private final AmazonS3Service amazonS3Service;

  private final ObjectMapper objectMapper;

  @Transactional
  public void createPlane(User user, CreatePlaneForm createPlaneForm) {

    Plane plane = planeRepository.save(Plane.builder()
        .subject(createPlaneForm.getSubject())
        .content(createPlaneForm.getContent())
        .country(createPlaneForm.getCountry())
        .city(createPlaneForm.getCity())
        .viewCount(0L)
        .user(user)
        .build());
    plane.setThumbnailUrl(amazonS3Service.uploadForThumbnail(
        createPlaneForm.getThumbnail(), plane.getId()));

    for (int i = 0; i < createPlaneForm.getCreatePlaceFormList().size(); i++) {
      CreatePlaceForm createPlaceForm =
          createPlaneForm.getCreatePlaceFormList().get(i);

      placeRepository.save(Place.builder()
          .subject(createPlaceForm.getSubject())
          .content(createPlaceForm.getContent())
          .address(createPlaceForm.getAddress())
          .plane(plane)
          .pictureListUrl(
              amazonS3Service.uploadForPictureList(
                  createPlaceForm.getPictureList(), i, plane.getId())
          ).build());
    }
  }

  public Page<PlaneListDto> getPlaneList(User user, Pageable pageable) {

    return planeRepository.findAll(pageable)
        .map(plane -> {
          PlaneListDto planeListDto = PlaneListDto.fromEntity(plane);

          if (user != null) {
            bookmarkRepository.findByUserIdAndPlaneId(user.getId(), plane.getId())
                .ifPresent(bookmark -> planeListDto.setBookmark(true));
            likesRepository.findByUserIdAndPlaneId(user.getId(), plane.getId())
                .ifPresent(bookmark -> planeListDto.setBookmark(true));
          }

          return planeListDto;
        });
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
          .ifPresent(bookmark -> planeDto.setBookmark(true));
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

    planeRepository.delete(plane);
  }
}
