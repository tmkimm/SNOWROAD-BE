package com.snowroad.search.map.service;

import com.snowroad.search.map.domain.EventsGeoRepository;
import com.snowroad.search.map.dto.EventsGeoMapDto;
import com.snowroad.search.map.enums.GeoMapEnum;
import com.snowroad.search.map.interfaces.EventGeoMapQueryInterface;
import com.snowroad.search.map.util.BoundingBoxCalculator;
import com.snowroad.search.map.util.Haversine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventGeoMapQueryService implements EventGeoMapQueryInterface {

    private final EventsGeoRepository eventsGeoRepository;

    /**
     * @author hyo298, 김재효
     * @param latitude 위도
     * @param longitude 경도
     * @return double[]
     */
    @Override
    public List<EventsGeoMapDto> getMapList(double latitude, double longitude) {
        double[] calculateBoundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude);
        List<EventsGeoMapDto> candidates = eventsGeoRepository
                .findEventsGeoCustomPoint(calculateBoundingBox[0], calculateBoundingBox[1], calculateBoundingBox[2], calculateBoundingBox[3]);

        List<EventsGeoMapDto> result = new LinkedList<>();
        for (EventsGeoMapDto eventsGeoMapDto : candidates) {
            double distance = Haversine.calculateDistance(
                    latitude, longitude, eventsGeoMapDto.getAddrLttd(), eventsGeoMapDto.getAddrLotd()
            );

            if (distance <= GeoMapEnum.MAP_DISTANCE_STANDARD.getRate()) {
                log.info("거리 계산 :: {}",distance);
                log.info("이벤트명 :: {}",eventsGeoMapDto.getEvntNm());
                result.add(eventsGeoMapDto);
            }
        }
        return result;
    }
}
