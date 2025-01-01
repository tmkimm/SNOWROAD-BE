package com.snowroad.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileS3Uploader {
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException { // dirName의 디
        // 파일의 원본 이름을 가져옴
        String originalFileName = multipartFile.getOriginalFilename();

        // 파일 확장자 추출
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 고유한 UUID로 파일 이름 생성
        String uniqueFileName = FileNameGenerator.generateUniqueFileName() + fileExtension;

        return uploadToS3(multipartFile, dirName, uniqueFileName);
    }

    public String uploadToS3(MultipartFile multipartFile, String dirName, String fileName) throws IOException {
        String contentType = multipartFile.getContentType();
        String key = dirName + "/" + fileName;
        // InputStream을 통해 파일을 스트리밍 방식으로 처리
        try (InputStream inputStream = multipartFile.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);  // MIME 타입을 메타데이터에 설정
            metadata.setContentLength(multipartFile.getSize());  // 파일 크기 설정

            // S3에 파일을 스트리밍 방식으로 업로드
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, inputStream, metadata);
            amazonS3Client.putObject(putObjectRequest);
            log.info("파일 업로드 완료");
        } catch (IOException e) {
            log.error("파일 업로드 실패: " + e.getMessage());
            throw e;
        }
        return amazonS3Client.getUrl(bucket, key).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일을 삭제하지 못했습니다.");
        }
    }

}
