package com.maptravel.maptravel.service;

import static com.maptravel.maptravel.domain.constants.ProviderType.LOCAL;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.maptravel.maptravel.domain.constants.RoleType;
import com.maptravel.maptravel.domain.entity.Bookmark;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.BookmarkRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@DisplayName("여행 게시물 북마크 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class BookmarkServiceTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlaneRepository planeRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private BookmarkRepository bookmarkRepository;

  private final String url = "http://localhost:8080";
  private final String THUMBNAIL = "thumbnail";

  @AfterEach
  void init() {
    bookmarkRepository.deleteAll();
    planeRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("여행 게시물 북마크 추가 - 성공")
  @Test
  void addBookmark_Success() throws Exception {
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
            post(url + "/v1/plane/" + plane.getId() + "/bookmark")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    Bookmark bookmark = bookmarkRepository.findAllByUserId(
        user.getId(), Pageable.ofSize(20)).getContent().get(0);

    assertEquals(plane.getId(), bookmark.getPlane().getId());
    assertEquals(user.getId(), bookmark.getUser().getId());
  }

  @DisplayName("여행 게시물 조회 - 성공")
  @Test
  void getBookmark_Success() throws Exception {
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
    Bookmark bookmark = bookmarkRepository.save(Bookmark.builder()
        .plane(plane)
        .user(user)
        .build());
    mockMvc.perform(
            get(url + "/v1/plane" + "/bookmark")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].id",
                equalTo(Integer.parseInt(bookmark.getId().toString())))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].planeId",
                equalTo(Integer.parseInt(plane.getId().toString())))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].subject",
                equalTo(plane.getSubject()))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].content",
                equalTo(plane.getContent()))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].country",
                equalTo(plane.getCountry()))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].city",
                equalTo(plane.getCity()))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].thumbnailUrl",
                equalTo(plane.getThumbnailUrl()))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].viewCount",
                equalTo(Integer.parseInt(plane.getViewCount().toString())))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].userNickname",
                equalTo(user.getNickname()))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].userProfileUrl",
                equalTo(user.getProfileImageUrl()))
        )
        .andDo(MockMvcResultHandlers.print());

  }

  @DisplayName("여행 게시물 북마크 삭제 - 성공")
  @Test
  void deleteBookmark_Success() throws Exception {
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
    Bookmark bookmark = bookmarkRepository.save(Bookmark.builder()
        .plane(plane)
        .user(user)
        .build());

    mockMvc.perform(
            delete(url + "/v1/plane" + "/bookmark/" + bookmark.getId())
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    long totalElements = bookmarkRepository.findAllByUserId(
        user.getId(), Pageable.ofSize(20)).getTotalElements();

    assertEquals(totalElements, 0L);
  }


}