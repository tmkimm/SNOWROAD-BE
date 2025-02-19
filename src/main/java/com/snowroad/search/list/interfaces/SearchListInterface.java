package com.snowroad.search.list.interfaces;

import com.snowroad.search.list.domain.SearchListResponseDTO;

import java.util.List;

/**
 *
 * 검색 인터페이스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-14
 *
 */
public interface SearchListInterface {
    List<SearchListResponseDTO> searchText(String keyword);
}
