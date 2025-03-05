package com.snowroad.MorphemeAnalyzer.komoran.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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

    private final KomoranProperties komoranProperties;
    private final ResourceLoader resourceLoader;

    @Bean
    public Komoran komoran() {
        Komoran komoran = new Komoran(DEFAULT_MODEL.STABLE);
        try {
            Resource resource = resourceLoader.getResource(komoranProperties.getDictionaryPath());
            File tempFile = File.createTempFile("komoran-dic", ".user");
            InputStream inputStream = resource.getInputStream();
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            komoran.setUserDic(tempFile.getAbsolutePath());
            log.info("사용자 사전 로드 성공: {}", tempFile.getAbsolutePath());
        }catch (Exception e) {
            throw new IllegalStateException("사용자 사전 로드 실패", e);
        }
        return komoran;
    }
}

