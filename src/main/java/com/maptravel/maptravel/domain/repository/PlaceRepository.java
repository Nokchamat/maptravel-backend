package com.maptravel.maptravel.domain.repository;

import com.maptravel.maptravel.domain.entity.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

  List<Place> findAllByPlaneId(Long planeId);
}
