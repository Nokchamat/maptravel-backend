package com.maptravel.maptravel.controller;

import static com.maptravel.maptravel.domain.constants.ProviderType.LOCAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.maptravel.maptravel.domain.constants.RoleType;
import com.maptravel.maptravel.domain.entity.Likes;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.LikesRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@DisplayName("여행 게시물 좋아요 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class LikesControllerTest {

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

  private final String url = "http://localhost:8080";
  private final String THUMBNAIL = "thumbnail";

  @AfterEach
  void init() {
    likesRepository.deleteAll();
    planeRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("여행 게시물 좋아요 추가 - 성공")
  @Test
  void addLikes_Success() throws Exception {
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
            post(url + "/v1/plane/" + plane.getId() + "/likes")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    Likes likes = likesRepository.findAllByUserId(
        user.getId(), Pageable.ofSize(20)).getContent().get(0);

    assertEquals(plane.getId(), likes.getPlane().getId());
    assertEquals(user.getId(), likes.getUser().getId());
  }

  @DisplayName("여행 게시물 좋아요 삭제 - 성공")
  @Test
  void deleteLikes_Success() throws Exception {
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
    likesRepository.save(Likes.builder()
        .plane(plane)
        .user(user)
        .build());

    mockMvc.perform(
            delete(url + "/v1/plane/" + plane.getId() + "/likes")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    long totalElements = likesRepository.findAllByUserId(
        user.getId(), Pageable.ofSize(20)).getTotalElements();

    assertEquals(totalElements, 0L);
  }


}