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
            String dicPath = komoranProperties.getDictionaryPath();
            Assert.hasText(dicPath, "Dictionary path must not be empty");

            // classpath에서 사용자 사전 로드
            ClassPathResource resource = new ClassPathResource(dicPath.replace("classpath:", ""));

            if (!resource.exists()) {
                throw new IllegalStateException("사용자 사전 리소스를 찾을 수 없음: " + dicPath);
            }

            File file;
            try {
                file = resource.getFile(); // jar 밖에서는 잘 동작
            } catch (IOException e) {
                // jar로 패키징된 경우, resource.getFile()은 실패함 → 임시파일로 복사
                file = Files.createTempFile("komoran-dic", ".user").toFile();
                try (InputStream inputStream = resource.getInputStream()) {
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            komoran.setUserDic(file.getAbsolutePath());

            log.info("사용자 사전 로드 성공: {}", file.getAbsolutePath());
        } catch (Exception e) {
            throw new IllegalStateException("사용자 사전 로드 실패", e);
        }
        return komoran;
    }
}
