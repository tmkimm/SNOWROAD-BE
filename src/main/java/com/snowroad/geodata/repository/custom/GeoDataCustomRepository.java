package com.snowroad.geodata.repository.custom;

import com.snowroad.entity.Events;

import java.util.List;

/**
 *
 * 거리표준검색 검색 기능
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-04-07
 *
 */
public interface GeoDataCustomRepository {
    List<Events> findGeoMapDataList(double minLat, double maxLat, double minLon, double maxLon);
}
