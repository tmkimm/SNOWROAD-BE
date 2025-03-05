package com.snowroad.search.map.repository.custom;

import com.snowroad.search.map.dto.SearchMapRequestDTO;
import com.snowroad.search.map.dto.SearchMapResponseDTO;

import java.util.List;

/**
 *
 * Map 검색 커스텀 레포
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-28
 *
 */
public interface SearchMapCustomRepository {
    List<SearchMapResponseDTO> findLocationMapDataList(SearchMapRequestDTO requestDTO);
}
