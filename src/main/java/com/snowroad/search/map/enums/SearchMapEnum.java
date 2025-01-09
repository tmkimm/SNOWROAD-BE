package com.snowroad.search.map.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * Map 검색 Enum
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-09
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
