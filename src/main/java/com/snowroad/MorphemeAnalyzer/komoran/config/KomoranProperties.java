package com.snowroad.MorphemeAnalyzer.komoran.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * 형태소 분석기 설정
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-15
 *
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "komoran")
public class KomoranProperties {
    private String dictionaryPath;
}
