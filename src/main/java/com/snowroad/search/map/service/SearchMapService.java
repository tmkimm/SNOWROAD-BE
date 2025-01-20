package com.snowroad.search.map.service;

import com.snowroad.search.map.domain.SearchMapRepository;
import com.snowroad.search.map.dto.SearchMapResponseDTO;
import com.snowroad.search.map.enums.SearchMapEnum;
import com.snowroad.search.map.interfaces.SearchMapInterface;
import com.snowroad.search.map.util.BoundingBoxCalculator;
import com.snowroad.search.map.util.HaversineFormula;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Map 검색 서비스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-09
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SearchMapService implements SearchMapInterface {

    private final SearchMapRepository searchMapRepository;

    /**
     * @author hyo298, 김재효
     * @param latitude 위도
     * @param longitude 경도
     * @return double[]
     */
    @Override
    public List<SearchMapResponseDTO> getMapList(double latitude, double longitude) {
        double[] calculateBoundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude);
        List<SearchMapResponseDTO> candidates = searchMapRepository
                .findEventsGeoCustomPoint(calculateBoundingBox[0], calculateBoundingBox[1], calculateBoundingBox[2], calculateBoundingBox[3]);

        List<SearchMapResponseDTO> result = new LinkedList<>();
        for (SearchMapResponseDTO searchMapResponseDTO : candidates) {
            double distance = HaversineFormula.calculateDistance(
                    latitude, longitude, searchMapResponseDTO.getAddrLttd(), searchMapResponseDTO.getAddrLotd()
            );

            if (distance <= SearchMapEnum.MAP_DISTANCE_STANDARD.getRate()) {
                log.info("""
                                
                                ========== START LOG ==========
                                거리 값: {}
                                이벤트 명: {}
                                ========== END LOG ==========""",
                        distance, searchMapResponseDTO.getEvntNm());
                result.add(searchMapResponseDTO);
            }
        }
        return result;
    }
}
