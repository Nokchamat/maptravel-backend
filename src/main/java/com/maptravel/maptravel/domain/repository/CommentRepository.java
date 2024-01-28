package com.maptravel.maptravel.domain.repository;

import com.maptravel.maptravel.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  Long countByPlaneId(Long planeId);

  Page<Comment> findAllByPlaneId(Long planeId, Pageable pageable);

  void deleteAllByUserId(Long userId);
}
