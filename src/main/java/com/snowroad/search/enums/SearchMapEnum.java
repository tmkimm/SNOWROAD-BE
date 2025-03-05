package com.snowroad.search.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 검색 Map enum
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-03-05
 *
 */
@Getter
@AllArgsConstructor
public enum SearchMapEnum {

    /**
     * 기본 거리 표준 2KM
     */
    MAP_DISTANCE_STANDARD(2.0);

    private final double rate;
}
