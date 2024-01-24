package com.maptravel.maptravel.service;

import com.maptravel.maptravel.domain.dto.CommentDto;
import com.maptravel.maptravel.domain.entity.Comment;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.CommentRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.exception.CustomException;
import com.maptravel.maptravel.exception.ErrorCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  private final PlaneRepository planeRepository;

  public void createComment(User user, Long planeId, String comment) {
    Plane plane = planeRepository.findById(planeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PLANE));

    commentRepository.save(Comment.builder()
        .comment(comment)
        .plane(plane)
        .user(user)
        .build());
  }

  public void deleteComment(User user, Long commentId) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.ALREADY_DELETE_COMMENT));

    if (!Objects.equals(comment.getUser().getId(), user.getId())) {
      throw new CustomException(ErrorCode.PERMISSION_DENIED);
    }

    commentRepository.delete(comment);
  }

  public Page<CommentDto> getComment(Long planeId, Pageable pageable) {

    return commentRepository.findAllByPlaneId(planeId, pageable)
        .map(CommentDto::fromEntity);
  }
}
