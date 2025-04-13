package com.snowroad.geodata.service;

import com.snowroad.entity.Events;
import com.snowroad.geodata.interfaces.GeoDataInterface;
import com.snowroad.geodata.repository.GeoDataRepository;
import com.snowroad.geodata.util.BoundingBoxCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        // step.1 : 이벤트 ID를 기준으로 바운딩 박스 생성
        Events events = geoDataRepository.findByEventId(eventId);
        Double latitude = events.getAddrLttd();
        Double longitude = events.getAddrLotd();

        // step.2 : 좌표를 기준으로 바운딩 박스 생성
        double[] calculateBoundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude);

        return  geoDataRepository.findGeoMapDataList(calculateBoundingBox[0], calculateBoundingBox[1]
                , calculateBoundingBox[2], calculateBoundingBox[3]);
    }
}
