package com.snowroad.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

// 파일명 생성기
public class FileNameGenerator {
//    public static String generateUniqueFileName() {
//        // UUID 생성
//        UUID uuid = UUID.randomUUID();  // TODO UUID 파일명을 더 짧게 변경
//
//        return uuid.toString();
//    }

    // 파일 이름 생성 메서드
    public static String generateUniqueFileName(MultipartFile image) {
        // 파일의 원본 이름을 가져옴
        String originalFileName = image.getOriginalFilename();

        // 파일 확장자 추출
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // UUID 생성
        UUID uuid = UUID.randomUUID();  // TODO UUID 파일명을 더 짧게 변경

        // 고유한 UUID로 파일 이름 생성
        return uuid.toString() + fileExtension;
    }


}
