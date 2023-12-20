package com.maptravel.maptravel.controller;

import static com.maptravel.maptravel.domain.constants.ProviderType.LOCAL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.maptravel.maptravel.domain.constants.RoleType;
import com.maptravel.maptravel.domain.entity.Place;
import com.maptravel.maptravel.domain.entity.Plane;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.form.CreatePlaceForm;
import com.maptravel.maptravel.domain.form.CreatePlaneForm;
import com.maptravel.maptravel.domain.repository.PlaceRepository;
import com.maptravel.maptravel.domain.repository.PlaneRepository;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import com.maptravel.maptravel.service.AmazonS3Service;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("여행 게시물 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class PlaneControllerTest {

  @MockBean
  private AmazonS3Service amazonS3Service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlaneRepository planeRepository;

  @Autowired
  private PlaceRepository placeRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private final String url = "http://localhost:8080";
  private final String THUMBNAIL = "thumbnail";
  private final String PICTURE = "picture";

  private User user;

  @AfterEach
  void init() {
    placeRepository.deleteAll();
    planeRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("여행 게시물 작성 - 성공")
  @Test
  void createPlane_Success() throws Exception {

    List<MultipartFile> multipartFileList = new ArrayList<>();
    multipartFileList.add(new MockMultipartFile(
        "picture00", "picture00.png", "png",
        "test file".getBytes(StandardCharsets.UTF_8)));
    multipartFileList.add(new MockMultipartFile(
        "picture01", "picture01.png", "png",
        "test file".getBytes(StandardCharsets.UTF_8)));

    when(amazonS3Service.uploadForThumbnail(any(), any(), any()))
        .thenReturn(THUMBNAIL);
    when(amazonS3Service.uploadForPictureList(any(), anyInt(), any(), any()))
        .thenReturn(PICTURE);

    CreatePlaceForm createPlaceForm0 = CreatePlaceForm.builder()
        .subject("subject0")
        .content("content0")
        .address("address0")
        .pictureList(multipartFileList)
        .build();
    CreatePlaceForm createPlaceForm1 = CreatePlaceForm.builder()
        .subject("subject1")
        .content("content1")
        .address("address1")
        .pictureList(multipartFileList)
        .build();

    user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    List<CreatePlaceForm> createPlaceFormList = new ArrayList<>();
    createPlaceFormList.add(createPlaceForm0);
    createPlaceFormList.add(createPlaceForm1);

    CreatePlaneForm createPlaneForm = CreatePlaneForm.builder()
        .subject("subject")
        .content("content")
        .country("country")
        .city("city")
        .thumbnail(
            new MockMultipartFile("thumbnail", "thumbnail.png", "png",
                "test file".getBytes(StandardCharsets.UTF_8)))
        .createPlaceFormList(createPlaceFormList)
        .build();

    mockMvc.perform(
            multipart(url + "/v1/place")
                .file("thumbnail", createPlaneForm.getThumbnail().getBytes())
                .file("createPlaceFormList[0].pictureList",
                    createPlaceFormList.get(0).getPictureList().get(0).getBytes())
                .file("createPlaceFormList[0].pictureList",
                    createPlaceFormList.get(0).getPictureList().get(1).getBytes())
                .file("createPlaceFormList[1].pictureList",
                    createPlaceFormList.get(1).getPictureList().get(0).getBytes())
                .file("createPlaceFormList[1].pictureList",
                    createPlaceFormList.get(1).getPictureList().get(1).getBytes())
                .param("subject", createPlaneForm.getSubject())
                .param("content", createPlaneForm.getContent())
                .param("country", createPlaneForm.getCountry())
                .param("city", createPlaneForm.getCity())
                .param("createPlaceFormList[0].subject",
                    createPlaceFormList.get(0).getSubject())
                .param("createPlaceFormList[0].content",
                    createPlaceFormList.get(0).getContent())
                .param("createPlaceFormList[0].address",
                    createPlaceFormList.get(0).getAddress())
                .param("createPlaceFormList[1].subject",
                    createPlaceFormList.get(1).getSubject())
                .param("createPlaceFormList[1].content",
                    createPlaceFormList.get(1).getContent())
                .param("createPlaceFormList[1].address",
                    createPlaceFormList.get(1).getAddress())
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    Plane planes = planeRepository.findAll().get(0);
    List<Place> placeList = placeRepository.findAllByPlaneId(planes.getId());
    assertEquals(createPlaneForm.getSubject(), planes.getSubject());
    assertEquals(createPlaneForm.getContent(), planes.getContent());
    assertEquals(THUMBNAIL, planes.getThumbnailUrl());
    assertEquals(createPlaceForm0.getSubject(), placeList.get(0).getSubject());
    assertEquals(createPlaceForm0.getContent(), placeList.get(0).getContent());
    assertEquals(createPlaceForm0.getAddress(), placeList.get(0).getAddress());
    assertEquals(PICTURE, placeList.get(0).getPictureListUrl());
    assertEquals(createPlaceForm1.getSubject(), placeList.get(1).getSubject());
    assertEquals(createPlaceForm1.getContent(), placeList.get(1).getContent());
    assertEquals(createPlaceForm1.getAddress(), placeList.get(1).getAddress());
    assertEquals(PICTURE, placeList.get(1).getPictureListUrl());
  }

  @DisplayName("여행 게시물 리스트 조회 - 성공")
  @Test
  void getPlaneList_Success() throws Exception {

    user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    planeRepository.save(Plane.builder()
        .subject("subject")
        .content("content")
        .viewCount(0L)
        .thumbnailUrl(THUMBNAIL)
        .user(user)
        .build());
    planeRepository.save(Plane.builder()
        .subject("subject")
        .content("content")
        .viewCount(0L)
        .thumbnailUrl(THUMBNAIL)
        .user(user)
        .build());

    mockMvc.perform(
            get(url + "/v1/place")
        ).andExpect(
            status().isOk()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content", hasSize(2))
        )
        .andDo(MockMvcResultHandlers.print());

  }

  @DisplayName("여행 게시물 상세 조회 - 성공")
  @Test
  void getPlaneDetail_Success() throws Exception {

    user = userRepository.save(User.builder()
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
        .country("country")
        .city("city")
        .viewCount(0L)
        .thumbnailUrl(THUMBNAIL)
        .user(user)
        .build());

    Place place = placeRepository.save(Place.builder()
        .subject("placeSubject")
        .content("placeContent")
        .address("placeAddress")
        .pictureListUrl("[\"" + PICTURE + "\"]")
        .plane(plane)
        .build());

    mockMvc.perform(
            get(url + "/v1/place/" + plane.getId())
        ).andExpect(
            status().isOk()
        ).andExpect(
            jsonPath("$.id", equalTo(Integer.parseInt(plane.getId().toString())))
        ).andExpect(
            jsonPath("$.subject", equalTo(plane.getSubject()))
        ).andExpect(
            jsonPath("$.content", equalTo(plane.getContent()))
        ).andExpect(
            jsonPath("$.country", equalTo(plane.getCountry()))
        ).andExpect(
            jsonPath("$.city", equalTo(plane.getCity()))
        ).andExpect(
            jsonPath("$.viewCount",
                equalTo(Integer.parseInt(plane.getViewCount().toString()) + 1))
        ).andExpect(
            jsonPath("$.thumbnailUrl", equalTo(plane.getThumbnailUrl()))
        ).andExpect(
            jsonPath("$.userNickname", equalTo(user.getNickname()))
        ).andExpect(
            jsonPath("$.userProfileImageUrl", equalTo(user.getProfileImageUrl()))
        ).andExpect(
            jsonPath("$.placeDtoList[0].id",
                equalTo(Integer.parseInt(place.getId().toString())))
        ).andExpect(
            jsonPath("$.placeDtoList[0].subject", equalTo(place.getSubject()))
        ).andExpect(
            jsonPath("$.placeDtoList[0].content", equalTo(place.getContent()))
        ).andExpect(
            jsonPath("$.placeDtoList[0].address", equalTo(place.getAddress()))
        ).andExpect(
            jsonPath("$.placeDtoList[0].pictureUrlArray[0]", equalTo(PICTURE))
        )
        .andDo(MockMvcResultHandlers.print());

  }

  @DisplayName("여행 게시물 삭제 - 성공")
  @Test
  void deletePlane_Success() throws Exception {

    user = userRepository.save(User.builder()
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
        .thumbnailUrl(THUMBNAIL)
        .user(user)
        .build());

    placeRepository.save(Place.builder()
        .subject("placeSubject")
        .content("placeContent")
        .address("placeAddress")
        .pictureListUrl("[\"" + PICTURE + "\"]")
        .plane(plane)
        .build());

    mockMvc.perform(
            delete(url + "/v1/place/" + plane.getId())
                .header(ACCESS_TOKEN,
                    jwtTokenProvider.generateToken(user.getEmail())
                        .getAccessToken())
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    List<Plane> planeList = planeRepository.findAll();
    List<Place> placeList = placeRepository.findAll();

    assertTrue(planeList.isEmpty());
    assertTrue(placeList.isEmpty());

  }

}