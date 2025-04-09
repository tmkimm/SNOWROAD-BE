package com.snowroad.MorphemeAnalyzer.komoran.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.InputStream;

/**
 *
 * 형태소 분석기 빈 관리
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-15
 *
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class KomoranConfig {
    @Bean
    public Komoran komoran(@Value("classpath:dictionary/komoran-dictionary.user") Resource resource) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.STABLE);
        try {
            if (!resource.exists()) {
                throw new IllegalStateException("사용자 사전 리소스를 찾을 수 없음");
            }

            // 임시파일로 복사
            File tempFile = Files.createTempFile("komoran-dic", ".user").toFile();
            try (InputStream inputStream = resource.getInputStream()) {
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            komoran.setUserDic(tempFile.getAbsolutePath());
            log.info("사용자 사전 로드 성공: {}", tempFile.getAbsolutePath());

        } catch (IOException e) {
            throw new IllegalStateException("사용자 사전 로드 실패", e);
        }
        return komoran;
    }
}

