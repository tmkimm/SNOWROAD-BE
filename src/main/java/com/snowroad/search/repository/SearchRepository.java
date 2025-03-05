package com.snowroad.search.repository;

import com.snowroad.entity.Events;
import com.snowroad.search.repository.custom.SearchCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * 검색 레포지토리
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-03-05
 *
 */
public interface SearchRepository extends JpaRepository<Events, Long>, SearchCustomRepository {

}
