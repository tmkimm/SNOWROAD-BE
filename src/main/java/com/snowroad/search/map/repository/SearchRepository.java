package com.snowroad.search.map.repository;

import com.snowroad.entity.Events;
import com.snowroad.search.map.repository.custom.SearchCustomRepository;
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
public interface SearchRepository extends JpaRepository<Events, Long>, SearchCustomRepository {

}
