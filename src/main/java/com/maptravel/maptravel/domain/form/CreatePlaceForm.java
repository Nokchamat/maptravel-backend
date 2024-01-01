package com.maptravel.maptravel.domain.form;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreatePlaceForm {

  private String subject;

  private String content;

  private String address;

  private Double latitude;

  private Double longitude;

  private List<MultipartFile> pictureList;

}
