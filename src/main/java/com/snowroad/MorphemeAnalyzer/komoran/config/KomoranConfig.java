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
    private final ResourceLoader resourceLoader;

    @Bean
    public Komoran komoran() {
        Komoran komoran = new Komoran(DEFAULT_MODEL.STABLE);
        try {
            String dicPath = komoranProperties.getDictionaryPath();
            dicPath = "classpath:/dictionary/komoran-dictionary.user";
            Assert.hasText(dicPath, "Dictionary path must not be empty");

            // classpath 여부 확인 후 적절한 리소스 로딩
            Resource resource = dicPath.startsWith("classpath:")
                    ? new ClassPathResource(dicPath.replace("classpath:", ""))
                    : resourceLoader.getResource(dicPath);

            log.info("사용자 사전 파일 경로: {}", dicPath);
            log.info("리소스 존재 여부: {}", resource.exists());

            if (!resource.exists()) {
                throw new IllegalStateException("사용자 사전 리소스를 찾을 수 없음: " + dicPath);
            }

            // OS에 관계없이 임시 파일 생성 (Windows, Linux, macOS 대응)
            File tempFile = Files.createTempFile("komoran-dic", ".user").toFile();
            try (InputStream inputStream = resource.getInputStream()) {
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            komoran.setUserDic(tempFile.getAbsolutePath());
            log.info("사용자 사전 로드 성공: {}", tempFile.getAbsolutePath());

        } catch (Exception e) {
            throw new IllegalStateException("사용자 사전 로드 실패", e);
        }
        return komoran;
    }
}

