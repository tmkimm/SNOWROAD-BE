package com.snowroad.search.repository.custom;

import com.snowroad.entity.Events;
import com.snowroad.search.dto.SearchRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Page<Events> findSearchEventDataList(Pageable pageable, SearchRequestDTO requestDTO);
}
