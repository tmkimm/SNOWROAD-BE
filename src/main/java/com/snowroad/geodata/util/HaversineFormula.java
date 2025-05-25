package com.snowroad.geodata.util;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 하버사인 공식
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-09
 *
 */
@Slf4j
public class HaversineFormula {

    private static final double EARTH_RADIUS_KM = 6371.0; // 지구 반지름 (킬로미터)

    /**
     *
     * 하버사인 공식(Haversine Formula)을 사용하여 두 지리적 좌표(위도 및 경도) 사이의 대략적인 대원거리(Great-circle distance)를 계산합니다
     *
     * @author hyo298, 김재효
     * @param baseLat 기준 위치의 위도 (Latitude of the base location, in degrees)
     * @param baseLon 기준 위치의 경도 (Longitude of the base location, in degrees)
     * @param targetLat 대상 위치의 위도 (Latitude of the target location, in degrees)
     * @param targetLon 대상 위치의 경도 (Longitude of the target location, in degrees)
     * @return double
     */
    public static double calculateDistance(double baseLat, double baseLon, double targetLat, double targetLon) {

        // 좌표에 대한 입력 값 검증
        // 범위 초과시 에러 반환
        if (baseLat < -90 || baseLat > 90 || targetLat < -90 || targetLat > 90
                || baseLon < -180 || baseLon > 180 || targetLon < -180 || targetLon > 180) {
            log.error("Invalid latitude or longitude values");
        }

        double baseLatRad = Math.toRadians(baseLat);
        double baseLonRad = Math.toRadians(baseLon);
        double targetLatRad = Math.toRadians(targetLat);
        double targetLonRad = Math.toRadians(targetLon);

        double deltaLat = targetLatRad - baseLatRad;
        double deltaLon = targetLonRad - baseLonRad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(baseLatRad) * Math.cos(targetLatRad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
