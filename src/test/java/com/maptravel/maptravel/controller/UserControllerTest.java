package com.maptravel.maptravel.controller;

import static com.maptravel.maptravel.domain.constants.ProviderType.LOCAL;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.maptravel.maptravel.domain.constants.RoleType;
import com.maptravel.maptravel.domain.entity.Bookmark;
import com.maptravel.maptravel.domain.entity.Comment;
import com.maptravel.maptravel.domain.entity.Likes;
import com.maptravel.maptravel.domain.entity.Place;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.repository.BookmarkRepository;
import com.maptravel.maptravel.domain.repository.CommentRepository;
import com.maptravel.maptravel.domain.repository.LikesRepository;
import com.maptravel.maptravel.domain.repository.PlaceRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.exception.ErrorCode;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import com.maptravel.maptravel.service.AmazonS3Service;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@DisplayName("회원가입 및 로그인 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlaneRepository planeRepository;

  @Autowired
  private PlaceRepository placeRepository;

  @Autowired
  private LikesRepository likesRepository;

  @Autowired
  private BookmarkRepository bookmarkRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private AmazonS3Service amazonS3Service;

  private final String url = "http://localhost:8080";

  @BeforeEach
  void init() {
    likesRepository.deleteAll();
    commentRepository.deleteAll();
    bookmarkRepository.deleteAll();

    placeRepository.deleteAll();
    planeRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("이메일 인증 - 성공")
  @Test
  void verifyEmail_Success() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(false)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    mockMvc.perform(
            post(url + "/v1/user/verify")
                .param("code", user.getEmailVerifyCode())
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    User verifiedUser = userRepository.findById(user.getId()).get();
    Assertions.assertTrue(verifiedUser.getIsEmailVerify());
  }

  @DisplayName("이메일 인증 - 실패 - 인증코드 비일치")
  @Test
  void verifyEmail_Fail() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(false)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    mockMvc.perform(
            post(url + "/v1/user/verify")
                .param("code", user.getEmailVerifyCode() + "A")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isBadRequest()
        ).andExpect(
            jsonPath("$.code",
                equalTo(ErrorCode.MISMATCH_EMAIL_VERIFY_CODE.toString()))
        )
        .andDo(MockMvcResultHandlers.print());

    User verifiedUser = userRepository.findById(user.getId()).get();
    Assertions.assertFalse(verifiedUser.getIsEmailVerify());
  }

  @DisplayName("내 프로필 조회 - 성공")
  @Test
  void getMyProfile_Success() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    mockMvc.perform(
            get(url + "/v1/user/myprofile")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("유저아이디로 프로필 조회 - 성공")
  @Test
  void getProfileByUserId_Success() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    mockMvc.perform(
            get(url + "/v1/user/" + user.getId() + "/profile")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("프로필 이미지 업데이트 - 성공")
  @Test
  void updateProfileImage_Success() throws Exception {
    when(amazonS3Service.uploadForProfile(any(), any()))
        .thenReturn("update thumbnail");

    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    mockMvc.perform(
            multipart(HttpMethod.PUT, url + "/v1/user/profileimage")
                .file("profileImage", "new thumbnail".getBytes())
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    User updatedUser = userRepository.findById(user.getId()).get();
    Assertions.assertEquals(updatedUser.getProfileImageUrl(),
        "update thumbnail");
  }

  @DisplayName("닉네임 수정 - 성공")
  @Test
  void updateNickname_Success() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    mockMvc.perform(
            multipart(HttpMethod.PUT, url + "/v1/user/nickname")
                .param("nickname", "new nickname")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    User updatedUser = userRepository.findById(user.getId()).get();
    Assertions.assertEquals(updatedUser.getNickname(),
        "new nickname");
  }

  @DisplayName("닉네임 수정 - 실패 - 중복된 이름")
  @Test
  void updateNickname_Fail_AlreadyExistNickname() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    userRepository.save(User.builder()
        .email("test2@test.com")
        .password("12341234")
        .nickname("중복된 이름")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    mockMvc.perform(
            multipart(HttpMethod.PUT, url + "/v1/user/nickname")
                .param("nickname", "중복된 이름")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isBadRequest()
        ).andExpect(
            jsonPath("$.code",
                equalTo(ErrorCode.ALREADY_EXIST_NICKNAME.toString()))
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("회원 탈퇴 - 성공")
  @Test
  void deleteAccount_Success() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    mockMvc.perform(
            delete(url + "/v1/user")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("회원 탈퇴 - 성공2 - 좋아요, 댓글, 북마크")
  @Test
  void deleteAccount_Success2() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    User user2 = userRepository.save(User.builder()
        .email("test2@test.com")
        .password("12341234")
        .nickname("닉네임2")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    Plane plane = planeRepository.save(Plane.builder()
        .subject("subject")
        .content("content")
        .country("country")
        .city("city")
        .viewCount(0L)
        .thumbnailUrl("THUMBNAIL")
        .user(user2)
        .build());

    placeRepository.save(Place.builder()
        .subject("placeSubject")
        .content("placeContent")
        .address("placeAddress")
        .pictureListUrl("[\"" + "PICTURE" + "\"]")
        .plane(plane)
        .build());

    likesRepository.save(Likes.builder()
        .plane(plane)
        .user(user)
        .build());
    bookmarkRepository.save(Bookmark.builder()
        .plane(plane)
        .user(user)
        .build());
    commentRepository.save(Comment.builder()
        .comment("댓글")
        .plane(plane)
        .user(user)
        .build());

    mockMvc.perform(
            delete(url + "/v1/user")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    Assertions.assertTrue(likesRepository.findAll().isEmpty());
    Assertions.assertTrue(bookmarkRepository.findAll().isEmpty());
    Assertions.assertTrue(commentRepository.findAll().isEmpty());
  }

  @DisplayName("회원 탈퇴 - 실패 - 여행 계획 미삭제")
  @Test
  void deleteAccount_Fail_DontRemovePlane() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .emailVerifyCode(RandomString.make(5))
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    Plane plane = planeRepository.save(Plane.builder()
        .subject("subject")
        .content("content")
        .country("country")
        .city("city")
        .viewCount(0L)
        .thumbnailUrl("THUMBNAIL")
        .user(user)
        .build());

    placeRepository.save(Place.builder()
        .subject("placeSubject")
        .content("placeContent")
        .address("placeAddress")
        .pictureListUrl("[\"" + "PICTURE" + "\"]")
        .plane(plane)
        .build());

    mockMvc.perform(
            delete(url + "/v1/user")
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isBadRequest()
        ).andExpect(
            jsonPath("$.code",
                equalTo(ErrorCode.PLEASE_REMOVE_PLANE_FIRST.toString()))
        )
        .andDo(MockMvcResultHandlers.print());
  }

}