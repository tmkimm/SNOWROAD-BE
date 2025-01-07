package com.snowroad.file.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor
@Service
public class FileLocalUploader {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String upload(MultipartFile file) throws IOException {
        // 파일의 원본 이름을 가져옴
        String originalFileName = file.getOriginalFilename();

        // 파일 확장자 추출
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 고유한 UUID로 파일 이름 생성
        String uniqueFileName = FileNameGenerator.generateUniqueFileName() + fileExtension;

        // 파일을 저장할 경로 설정
        Path path = Paths.get(uploadDir, uniqueFileName);

        // 디렉토리 생성 (디렉토리가 없으면 생성)
        Files.createDirectories(path.getParent());

        // 파일을 로컬 디렉토리에 저장
        file.transferTo(path);

        // 저장된 파일의 경로 반환
        return path.toString();
    }
}
