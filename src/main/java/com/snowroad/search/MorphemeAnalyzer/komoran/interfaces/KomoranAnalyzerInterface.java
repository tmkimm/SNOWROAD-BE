package com.snowroad.search.MorphemeAnalyzer.komoran.interfaces;

import com.snowroad.search.MorphemeAnalyzer.komoran.domain.KomoranDTO;

import java.util.List;
import java.util.Map;

/**
 *
 * Komoran 형태소 분석기 Interface
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-08
 *
 */
public interface KomoranAnalyzerInterface {
    Map<String, List<KomoranDTO>> komoranAnalyzerMap(String text);
}
