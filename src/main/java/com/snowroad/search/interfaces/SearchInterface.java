package com.snowroad.search.interfaces;

import com.snowroad.search.dto.SearchPagedResponse;
import com.snowroad.search.dto.SearchRequestDTO;

/**
 *
 * 검색 인터페이스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-03-05
 *
 */
public interface SearchInterface {
    SearchPagedResponse getEvents(SearchRequestDTO searchRequestDTO);
}