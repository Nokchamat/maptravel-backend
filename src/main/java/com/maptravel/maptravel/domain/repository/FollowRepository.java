package com.maptravel.maptravel.domain.repository;

import com.maptravel.maptravel.domain.entity.Follow;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

  Optional<Follow> findByFollowerIdAndFollowingId(Long followerId,
      Long followingId);

  Page<Follow> findByFollowerId(Long followerId, Pageable pageable);

  Long countByFollowingId(Long id);
}
