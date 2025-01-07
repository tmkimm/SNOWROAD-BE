package com.snowroad.file.service;

import java.util.UUID;

// 파일명 생성기
public class FileNameGenerator {
    public static String generateUniqueFileName() {
        // UUID 생성
        UUID uuid = UUID.randomUUID();  // TODO UUID 파일명을 더 짧게 변경

        return uuid.toString();
    }
}
