package com.maptravel.maptravel.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final ObjectMetadata objectMetadata = new ObjectMetadata();

  // TODO PREFIX 바꿔야함
  private final static String PREFIX = "PREFIX";

  private final AmazonS3Client amazonS3Client;

  public String uploadForProfile(MultipartFile profile, Long userId) {

    try {
      log.info("[uploadForProfile 시작]" + " userId : " + userId);

      objectMetadata.setContentType(profile.getContentType());
      objectMetadata.setContentLength(profile.getSize());

      String fileKey = "profile/" + userId;

      amazonS3Client.putObject(bucket, fileKey, profile.getInputStream(), objectMetadata);

      log.info("[uploadForProfile 완료]" + " userId : " + userId);
      return amazonS3Client.getUrl(bucket, fileKey).toString();

    } catch (IOException e) {
      e.printStackTrace();
      log.info(e.getMessage());
    }

    return "";
  }

  public void deleteUploadFile(String uploadedFileUrl) {
    log.info("[deleteUploadFile 시작]" + " uploadedFileUrl : " + uploadedFileUrl);

    amazonS3Client.deleteObject(bucket, uploadedFileUrl.substring(PREFIX.length()));

    log.info("[deleteUploadFile 완료]" + " uploadedFileUrl : " + uploadedFileUrl);
  }

}
