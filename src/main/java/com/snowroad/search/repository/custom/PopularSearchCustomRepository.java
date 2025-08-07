package com.snowroad.search.repository.custom;

import com.snowroad.search.dto.PopularSearch;

import java.time.Duration;
import java.util.List;

/**
 *
 * 인기검색어 커스텀 레포지토리
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-06-15
 *
 */
public interface PopularSearchCustomRepository {
    List<PopularSearch> findTopPopularKeywords(int limit, Duration duration);
}
