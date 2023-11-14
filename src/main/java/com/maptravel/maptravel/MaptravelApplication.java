package com.maptravel.maptravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class MaptravelApplication {

  public static void main(String[] args) {
    SpringApplication.run(MaptravelApplication.class, args);
  }

}
