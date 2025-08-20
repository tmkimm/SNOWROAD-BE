package com.snowroad.search.interfaces;

import com.snowroad.search.dto.PopularSearchResponse;

/**
 *
 * 인기검색어 인터페이스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-06-15
 *
 */
public interface PopularSearchInterface {
    PopularSearchResponse getPopularSearch();
}