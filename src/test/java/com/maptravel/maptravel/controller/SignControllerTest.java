package com.maptravel.maptravel.controller;

import static com.maptravel.maptravel.domain.constants.ProviderType.GOOGLE;
import static com.maptravel.maptravel.domain.constants.ProviderType.LOCAL;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maptravel.maptravel.domain.constants.RoleType;
import com.maptravel.maptravel.domain.entity.User;
import com.maptravel.maptravel.domain.form.SignInForm;
import com.maptravel.maptravel.domain.form.SignUpForm;
import com.maptravel.maptravel.domain.repository.UserRepository;
import com.maptravel.maptravel.exception.ErrorCode;
import com.maptravel.maptravel.service.SendEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@DisplayName("회원가입 및 로그인 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class SignControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @MockBean
  private SendEmailService sendEmailService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ObjectMapper objectMapper;

  private final String url = "http://localhost:8080";

  @BeforeEach
  void init() {
    userRepository.deleteAll();
  }

  @DisplayName("회원 가입 - 성공")
  @Test
  void signUp_Success() throws Exception {
    doNothing().when(sendEmailService).sendMail(any(), any());

    SignUpForm signUpForm = new SignUpForm(
        "test@test.com",
        "12341234",
        "이름",
        "닉네임"
    );

    mockMvc.perform(
            post(url + "/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpForm))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("로그인 - 성공")
  @Test
  void signIn_Success() throws Exception {
    userRepository.save(User.builder()
        .email("test@test.com")
        .password(passwordEncoder.encode("12341234"))
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    SignInForm signInForm = new SignInForm(
        "test@test.com",
        "12341234"
    );

    mockMvc.perform(
            post(url + "/v1/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInForm))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("로그인 - 실패 - 패스워드 틀림")
  @Test
  void signIn_Fail_MissMatchPassword() throws Exception {
    userRepository.save(User.builder()
        .email("test@test.com")
        .password(passwordEncoder.encode("12341234"))
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(LOCAL.name())
        .build());

    SignInForm signInForm = new SignInForm(
        "test@test.com",
        "123412345"
    );

    mockMvc.perform(
            post(url + "/v1/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInForm))
        ).andExpect(
            status().isBadRequest()
        ).andExpect(
            jsonPath("$.code",
                equalTo(ErrorCode.MISMATCH_EMAIL_PASSWORD.toString()))
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("로그인 - 실패 - 소셜 로그인 플랫폼 비일치")
  @Test
  void signIn_Fail_MismatchProvider() throws Exception {
    userRepository.save(User.builder()
        .email("test@test.com")
        .password(passwordEncoder.encode("12341234"))
        .nickname("닉네임")
        .name("이름")
        .profileImageUrl("profileUrl")
        .isEmailVerify(true)
        .role(RoleType.USER)
        .provider(GOOGLE.name())
        .build());

    SignInForm signInForm = new SignInForm(
        "test@test.com",
        "12341234"
    );

    mockMvc.perform(
            post(url + "/v1/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInForm))
        ).andExpect(
            status().isBadRequest()
        ).andExpect(
            jsonPath("$.code",
                equalTo(ErrorCode.MISMATCHED_PLATFORM.toString()))
        )
        .andDo(MockMvcResultHandlers.print());
  }
}