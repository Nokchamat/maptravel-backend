package com.maptravel.maptravel.domain.repository;

import com.maptravel.maptravel.domain.entity.Likes;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

  Optional<Likes> findByUserIdAndPlaneId(Long userId, Long planeId);

  Page<Likes> findAllByUserId(Long id, Pageable pageable);

  Long countByPlaneId(Long planeId);

  void deleteAllByPlaneId(Long planeId);
}
