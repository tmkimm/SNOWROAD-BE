package com.snowroad.search.repository.custom;

import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.dto.SearchResponseDTO;
import org.springframework.data.domain.Page;

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
    Page<SearchResponseDTO> findSearchEventDataList(SearchRequestDTO requestDTO);
}
