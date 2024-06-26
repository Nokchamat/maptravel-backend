package com.maptravel.maptravel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.OAS_30)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.maptravel.maptravel.controller"))
        .paths(PathSelectors.any())
        .build();
  }

  public ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("MapTravel")
        .description("여행 게시물을 작성하고 그 위치를 지도로 한 번에 볼 수 있는 어플리케이션 입니다.")
        .version("1.0")
        .build();
  }
}
