package com.snowroad.MorphemeAnalyzer.komoran.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

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

    private final KomoranProperties komoranProperties;

    @Bean
    public Komoran komoran() {
        Komoran komoran = new Komoran(DEFAULT_MODEL.STABLE);

        try {
            // 사용자 사전 파일을 리소스에서 가져와서 실제 File 객체로 변환
            String dicPath = komoranProperties.getDictionaryPath(); // 예: "dictionary/komoran-dictionary.user"
            dicPath = "dictionary/komoran-dictionary.user";
            Assert.hasText(dicPath, "Dictionary path must not be empty");

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(dicPath);
            if (inputStream == null) {
                throw new FileNotFoundException("사용자 사전 리소스를 찾을 수 없음: " + dicPath);
            }

            // 임시 파일로 저장 (모든 OS 대응)
            File tempFile = Files.createTempFile("komoran-dic", ".user").toFile();
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            komoran.setUserDic(tempFile.getAbsolutePath());

            log.info("사용자 사전 로드 성공: {}", tempFile.getAbsolutePath());

        } catch (Exception e) {
            throw new IllegalStateException("사용자 사전 로드 실패", e);
        }

        return komoran;
    }
}