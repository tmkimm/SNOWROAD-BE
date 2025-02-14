package com.snowroad.MorphemeAnalyzer.komoran.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * KomoranPOS DTO
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-08
 *
 */
@Data
@Getter
@Setter
@Builder
public class KomoranDTO {
    private String token;
    private String posCode;
    private String posName;
}