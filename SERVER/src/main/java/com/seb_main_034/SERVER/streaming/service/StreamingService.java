package com.seb_main_034.SERVER.streaming.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.HttpMethod;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

@Service
public class StreamingService {

    // 지역을 AP_NORTHEAST_2 (서울)로 설정
    private AmazonS3 s3client = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.AP_NORTHEAST_2)
            .build();

    public ResponseEntity<Resource> getVideoStream(String videoPath) {
        try {
            File videoFile = new File(videoPath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(videoFile));

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("video/mp4"))
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    public String getStreamingUrl(String fileName) {
        // 버킷 이름을 "034a"로 설정
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest("034a", fileName);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}

