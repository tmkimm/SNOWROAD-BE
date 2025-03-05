package com.snowroad.search.repository.custom;

import com.snowroad.entity.Events;
import com.snowroad.search.dto.SearchRequestDTO;

import java.util.List;

/**
 *
 * 검색 커스텀 레포지토리
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-03-05
 *
 */
public interface SearchCustomRepository {
    List<Events> findLocationMapDataList(SearchRequestDTO requestDTO);
}
