package com.snowroad.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String upload(MultipartFile multipartFile, String dirName, String fileName) throws IOException {
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

    public String update(MultipartFile multipartFile, String dirName, String fileName, String deleteFileName) throws IOException {
        // 기존 파일 삭제 후 추가
       this.deleteFile(deleteFileName);
       return this.upload(multipartFile, dirName, fileName);
    }

    // 파일 삭제 메서드
    public void deleteFile(String fileName) {
        try {
            // S3에서 파일 삭제
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            System.out.println("File deleted successfully from S3: " + fileName);
        } catch (Exception e) {
            System.err.println("Error deleting file from S3: " + e.getMessage());
        }
    }

}
