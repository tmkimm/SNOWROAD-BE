package com.snowroad.search.map.repository;

import com.snowroad.search.map.dto.SearchMapResponseDTO;
import com.snowroad.search.map.repository.custom.SearchMapCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * Map 검색 레포지토리
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-09
 *
 */
public interface SearchMapRepository extends JpaRepository<SearchMapResponseDTO, Long>, SearchMapCustomRepository {

}
