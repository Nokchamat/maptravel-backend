package com.maptravel.maptravel.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
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

  private final static String PREFIX = "https://bucket-maptravel.s3.ap-northeast-2.amazonaws.com/";

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

  public String uploadForThumbnail(MultipartFile thumbnail, Long planeId) {

    try {
      log.info("[uploadForThumbnail 시작]" + " planeId : " + planeId);

      objectMetadata.setContentType(thumbnail.getContentType());
      objectMetadata.setContentLength(thumbnail.getSize());

      String fileKey = "plane/" + planeId + "/thumbnail";

      amazonS3Client.putObject(bucket, fileKey, thumbnail.getInputStream(), objectMetadata);

      log.info("[uploadForThumbnail 완료]" + " planeId : " + planeId);
      return amazonS3Client.getUrl(bucket, fileKey).toString();
    } catch (IOException e) {
      e.printStackTrace();
      log.info(e.getMessage());
    }

    return "";
  }

  public String uploadForPictureList(List<MultipartFile> pictureList, int index, Long planeId) {

    try {
      log.info("[uploadForPictureList 시작]" + " planeId : " + planeId + ", index : " + index);
      JSONArray pictureUrlList = new JSONArray();

      for (int i = 0; i < pictureList.size(); i++) {
        objectMetadata.setContentType(pictureList.get(i).getContentType());
        objectMetadata.setContentLength(pictureList.get(i).getSize());

        String fileKey = "plane/" + planeId + "/" + index + "/" + i;

        amazonS3Client.putObject(bucket, fileKey, pictureList.get(i).getInputStream(),
            objectMetadata);
        pictureUrlList.add(amazonS3Client.getUrl(bucket, fileKey).toString());
      }

      log.info("[uploadForPictureList 완료]" + " planeId : " + planeId + ", index : " + index);
      return pictureUrlList.toString();
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

  public void deleteUploadFileArray(String[] uploadedFileArray) {
    log.info("[deleteUploadFileArray 시작]" + " uploadedFileArray : " + Arrays.toString(uploadedFileArray));

    for (String uploadedFileUrl : uploadedFileArray) {
      amazonS3Client.deleteObject(bucket, uploadedFileUrl.substring(PREFIX.length()));
    }

    log.info("[deleteUploadFileArray 완료]" + " uploadedFileArray : " + Arrays.toString(uploadedFileArray));
  }

}
