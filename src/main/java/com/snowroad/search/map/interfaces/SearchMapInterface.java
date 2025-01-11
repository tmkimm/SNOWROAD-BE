package com.snowroad.search.map.interfaces;

import com.snowroad.search.map.dto.SearchMapResponseDTO;

import java.util.List;

/**
 *
 * Map 검색 인터페이스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-09
 *
 */
public interface SearchMapInterface {
    List<SearchMapResponseDTO> getMapList(double latitude, double longitude);
}
