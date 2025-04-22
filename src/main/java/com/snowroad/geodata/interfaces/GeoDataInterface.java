package com.snowroad.geodata.interfaces;

import com.snowroad.entity.Events;

import java.util.List;

/**
 *
 * 거리표준검색 검색 인터페이스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-04-07
 *
 */
public interface GeoDataInterface {
    List<Events> getGeoData(Long eventId);
    List<Events> getGeoDataByLocation(double latitude, double longitude);
}
