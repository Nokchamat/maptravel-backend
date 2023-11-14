package com.maptravel.maptravel.config;

import com.maptravel.maptravel.oauth.handler.OAuth2SuccessHandler;
import com.maptravel.maptravel.oauth.service.CustomOAuth2UserService;
import com.maptravel.maptravel.oauth.jwt.JwtAuthenticationFilter;
import com.maptravel.maptravel.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final JwtTokenProvider jwtTokenProvider;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .authorizeRequests(auth -> auth
            .anyRequest().authenticated()
        )
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class)
        .oauth2Login(oauth ->
            oauth.userInfoEndpoint().userService(customOAuth2UserService)
                .and().successHandler(oAuth2SuccessHandler)
        )
    ;

    return http.build();
  }

}
