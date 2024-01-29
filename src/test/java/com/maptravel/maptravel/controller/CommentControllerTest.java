package com.maptravel.maptravel.controller;

import static com.maptravel.maptravel.domain.constants.ProviderType.LOCAL;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.maptravel.maptravel.domain.constants.RoleType;
import com.maptravel.maptravel.domain.entity.Comment;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.CommentRepository;
import com.maptravel.maptravel.domain.repository.LikesRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@DisplayName("댓글 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlaneRepository planeRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private LikesRepository likesRepository;

  @Autowired
  private CommentRepository commentRepository;

  private final String url = "http://localhost:8080";
  private final String THUMBNAIL = "thumbnail";

  @BeforeEach
  void init() {
    likesRepository.deleteAll();
    commentRepository.deleteAll();

    planeRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("댓글 작성 - 성공")
  @Test
  void createComment_Success() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());
    Plane plane = planeRepository.save(Plane.builder()
        .subject("subject")
        .content("content")
        .viewCount(0L)
        .country("country")
        .city("city")
        .thumbnailUrl(THUMBNAIL)
        .user(user)
        .build());
    mockMvc.perform(
            post(url + "/v1/plane/" + plane.getId() + "/comment")
                .param("comment", "댓글달기")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    Comment comment = commentRepository.findAll().get(0);
    assertEquals(comment.getComment(), "댓글달기");
  }

  @DisplayName("댓글 삭제 - 성공")
  @Test
  void deleteComment_Success() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());
    Plane plane = planeRepository.save(Plane.builder()
        .subject("subject")
        .content("content")
        .viewCount(0L)
        .country("country")
        .city("city")
        .thumbnailUrl(THUMBNAIL)
        .user(user)
        .build());
    Comment comment = commentRepository.save(Comment.builder()
        .comment("댓글 달기")
        .user(user)
        .plane(plane)
        .build());
    mockMvc.perform(
            delete(url + "/v1/comment/" + comment.getId())
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    assertTrue(commentRepository.findAll().isEmpty());
  }

  @DisplayName("댓글 조회 - 성공")
  @Test
  void getComment_Success() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());
    Plane plane = planeRepository.save(Plane.builder()
        .subject("subject")
        .content("content")
        .viewCount(0L)
        .country("country")
        .city("city")
        .thumbnailUrl(THUMBNAIL)
        .user(user)
        .build());
    commentRepository.save(Comment.builder()
        .comment("댓글 달기")
        .user(user)
        .plane(plane)
        .build());
    commentRepository.save(Comment.builder()
        .comment("댓글 달기")
        .user(user)
        .plane(plane)
        .build());
    mockMvc.perform(
            get(url + "/v1/plane/" + plane.getId() + "/comment")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content", hasSize(2))
        )
        .andDo(MockMvcResultHandlers.print());
  }

}