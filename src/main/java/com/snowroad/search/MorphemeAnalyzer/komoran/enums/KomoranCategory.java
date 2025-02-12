package com.snowroad.search.MorphemeAnalyzer.komoran.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * KomoranCategory
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-08
 *
 */
@Getter
@RequiredArgsConstructor
public enum KomoranCategory {

    NOMINAL("Nominal"),           // 체언
    VERBAL("Verbal"),             // 용언
    MODIFIER("Modifier"),         // 수식언
    INDEPENDENT("Independent"),   // 독립언
    RELATIONAL("Relational"),     // 관계언
    DEPENDENT("Dependent"),       // 의존형태
    SYMBOL("Symbol");             // 기호

    private final String groupKey;
}
