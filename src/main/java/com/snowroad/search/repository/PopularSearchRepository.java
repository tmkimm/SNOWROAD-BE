package com.snowroad.search.repository;

import com.snowroad.entity.Events;
import com.snowroad.search.repository.custom.PopularSearchCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * 인기검색어 레포지토리
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-06-15
 *
 */
public interface PopularSearchRepository extends JpaRepository<Events, Long>, PopularSearchCustomRepository {

}
