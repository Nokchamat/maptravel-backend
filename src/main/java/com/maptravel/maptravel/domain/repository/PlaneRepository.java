package com.maptravel.maptravel.domain.repository;

import com.maptravel.maptravel.domain.entity.Plane;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Long> {

  Page<Plane> findAll(Pageable pageable);
  Page<Plane> findAllByCountryOrCity(String country, String city, Pageable pageable);
  Page<Plane> findAllByUserId(Long userId, Pageable pageable);
}
