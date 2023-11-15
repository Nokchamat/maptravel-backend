package com.maptravel.maptravel.service;

import com.maptravel.maptravel.domain.entity.Place;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.form.CreatePlaceForm;
import com.maptravel.maptravel.domain.form.CreatePlaneForm;
import com.maptravel.maptravel.domain.repository.PlaceRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaneService {

  private final PlaneRepository planeRepository;

  private final PlaceRepository placeRepository;

  private final AmazonS3Service amazonS3Service;

  @Transactional
  public void createPlane(User user, CreatePlaneForm createPlaneForm) {

    Plane plane = planeRepository.save(Plane.builder()
        .subject(createPlaneForm.getSubject())
        .content(createPlaneForm.getContent())
        .user(user)
        .build());
    plane.setThumbnailUrl(
        amazonS3Service.uploadForThumbnail(createPlaneForm.getThumbnail(), plane.getId()));

    for (int i = 0; i < createPlaneForm.getCreatePlaceFormList().size(); i++) {
      CreatePlaceForm createPlaceForm = createPlaneForm.getCreatePlaceFormList().get(i);

      placeRepository.save(Place.builder()
          .subject(createPlaceForm.getSubject())
          .content(createPlaceForm.getContent())
          .address(createPlaceForm.getAddress())
          .plane(plane)
          .pictureUrl(
              amazonS3Service.uploadForPictureList(
                  createPlaceForm.getPictureList(), i, plane.getId())
          )
          .build());
    }


  }
}
