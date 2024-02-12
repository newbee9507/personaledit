package com.seb_main_034.SERVER.client;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AmazonClient {

    private AmazonS3 s3client = AmazonS3ClientBuilder.standard()
            .withRegion("ap-northeast-2") // 서울 리전
            .build();



    // ...생성자 및 설정 메서드

    public String uploadFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        try {
            File file = convertMultiPartToFile(multipartFile);
            uploadFileToS3Bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    private void uploadFileToS3Bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest("your-bucket-name", fileName, file));
    }
}
