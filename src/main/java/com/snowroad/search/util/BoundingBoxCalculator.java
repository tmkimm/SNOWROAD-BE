package com.snowroad.search.util;

import com.snowroad.search.enums.SearchMapEnum;

/**
 *
 * Bounding Box 계산
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-09
 *
 */
public class BoundingBoxCalculator {

    /**
     * @author hyo298, 김재효
     * @param latitude 위도
     * @param longitude 경도
     * @return double[]
     */
    public static double[] calculateBoundingBox(double latitude, double longitude) {

        // 위도 변화량 (1도 = 111km)
        double latRadius = SearchMapEnum.MAP_DISTANCE_STANDARD.getRate() / 111.0;

        // 경도 변화량 (위도에 따라 다름)
        double lonRadius = SearchMapEnum.MAP_DISTANCE_STANDARD.getRate() / (111.0 * Math.cos(Math.toRadians(latitude)));

        double minLat = latitude - latRadius;
        double maxLat = latitude + latRadius;
        double minLon = longitude - lonRadius;
        double maxLon = longitude + lonRadius;
        return new double[]{minLat, maxLat, minLon, maxLon};
    }
}
