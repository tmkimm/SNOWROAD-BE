package com.snowroad.search.map.domain;

import com.snowroad.search.map.dto.SearchMapResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * Map 검색 레포지토리
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-09
 *
 */
public interface SearchMapRepository extends JpaRepository<SearchMapResponseDTO, Long> {
    @Query("SELECT E FROM SearchListResponseDTO AS E WHERE E.addrLttd BETWEEN :minLat AND :maxLat AND E.addrLotd BETWEEN :minLon AND :maxLon  ")
    List<SearchMapResponseDTO> findEventsGeoCustomPoint(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon
    );
}
