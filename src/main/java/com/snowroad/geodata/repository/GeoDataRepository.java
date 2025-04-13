package com.snowroad.geodata.repository;

import com.snowroad.entity.Events;
import com.snowroad.geodata.repository.custom.GeoDataCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * 거리표준검색 레포지토리
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-04-07
 *
 */
public interface GeoDataRepository extends JpaRepository<Events, Long>, GeoDataCustomRepository {
    Events findByEventId(Long eventId);
}
