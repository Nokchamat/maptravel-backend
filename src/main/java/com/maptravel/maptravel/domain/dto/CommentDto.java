package com.maptravel.maptravel.domain.dto;

import com.maptravel.maptravel.domain.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto {

  private Long id;

  private String comment;

  private Long planeId;

  private Long userId;

  private String userNickname;

  private String userProfileImageUrl;

  private LocalDateTime createdAt;

  public static CommentDto fromEntity(Comment comment) {

    return CommentDto.builder()
        .id(comment.getId())
        .comment(comment.getComment())
        .planeId(comment.getId())
        .userId(comment.getUser().getId())
        .userNickname(comment.getUser().getNickname())
        .userProfileImageUrl(comment.getUser().getProfileImageUrl())
        .createdAt(comment.getCreatedAt())
        .build();
  }

}


