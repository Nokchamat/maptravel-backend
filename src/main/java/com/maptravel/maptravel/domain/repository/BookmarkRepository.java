package com.maptravel.maptravel.domain.repository;

import com.maptravel.maptravel.domain.entity.Bookmark;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
  Optional<Bookmark> findByUserIdAndPlaneId(Long userId, Long planeId);
  Page<Bookmark> findAllByUserId(Long id, Pageable pageable);
  void deleteAllByPlaneId(Long planeId);
  void deleteAllByUserId(Long userId);
}
