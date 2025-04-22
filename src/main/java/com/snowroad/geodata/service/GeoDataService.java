package com.snowroad.geodata.service;

import com.snowroad.entity.Events;
import com.snowroad.geodata.interfaces.GeoDataInterface;
import com.snowroad.geodata.repository.GeoDataRepository;
import com.snowroad.geodata.util.BoundingBoxCalculator;
import com.snowroad.geodata.util.HaversineFormula;
import com.snowroad.search.enums.SearchMapEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * 거리표준검색 구현체
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-04-07
 *
 */
@Service
@RequiredArgsConstructor
public class GeoDataService implements GeoDataInterface {

    private final GeoDataRepository geoDataRepository;

    @Override
    public List<Events> getGeoData(Long eventId) {
        if (eventId == null) {
            return null;
        }
        // 이벤트 ID를 기준으로 바운딩 박스 생성
        Events events = geoDataRepository.findByEventId(eventId);
        Double latitude = events.getAddrLttd();
        Double longitude = events.getAddrLotd();
        return getGeoDataByLocation(latitude, longitude);
    }

    @Override
    public List<Events> getGeoDataByLocation(double latitude, double longitude) {
        // 좌표를 기준으로 바운딩 박스 생성
        double[] calculateBoundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude);
        List<Events> getResponseList = geoDataRepository.findGeoMapDataList(calculateBoundingBox[0], calculateBoundingBox[1]
                , calculateBoundingBox[2], calculateBoundingBox[3]);
        List<Events> result = new LinkedList<>();
        for (Events response : getResponseList) {
            double distance = HaversineFormula.calculateDistance(
                    latitude, longitude, response.getAddrLttd(), response.getAddrLotd()
            );
            if (distance <= SearchMapEnum.MAP_DISTANCE_STANDARD.getRate()) {
                result.add(response);
            }
        }
        return result;
    }
}
