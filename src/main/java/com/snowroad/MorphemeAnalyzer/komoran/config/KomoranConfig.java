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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
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
//        Komoran komoran = new Komoran(DEFAULT_MODEL.STABLE);
//        try {
//            Resource resource = resourceLoader.getResource(komoranProperties.getDictionaryPath());
//            komoran.setUserDic(resource.getFile().getAbsolutePath());  // 직접 경로 설정
//            System.out.println("사용자 사전 로드 성공: " + resource.getFile().getAbsolutePath());
//        } catch (Exception e) {
//            throw new IllegalStateException("사용자 사전 로드 실패", e);
//        }

        // resource.getFile()로 class path내의 리소스를 로드하면 jar 파일 내부에 존재하는 리소스를 찾을 수 없음
        // spring boot의 getFile()은 파일 시스템의 경로를 반환하려고 하지만, jar 내부의 리소스는 파일이 아니라 클래스 패스 리소스로 존재
        // ❌즉 JAR 내부에서는 getFile() 사용 불가
        Komoran komoran = new Komoran(DEFAULT_MODEL.STABLE);
        try {
            Resource resource = resourceLoader.getResource(komoranProperties.getDictionaryPath());

            // InputStream을 임시 파일로 저장
            File tempFile = File.createTempFile("komoran-dic", ".user");
            try (InputStream inputStream = resource.getInputStream();
                 OutputStream outputStream = new FileOutputStream(tempFile)) {
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // Komoran에 사용자 사전 적용
            komoran.setUserDic(tempFile.getAbsolutePath());

            log.info("사용자 사전 로드 성공: {}", tempFile.getAbsolutePath());
        }catch (Exception e) {
            throw new IllegalStateException("사용자 사전 로드 실패", e);
        }
        return komoran;
    }
}

