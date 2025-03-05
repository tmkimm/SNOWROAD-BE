package com.snowroad.search.map.service;

import com.snowroad.search.map.repository.SearchMapRepository;
import com.snowroad.search.map.dto.SearchMapRequestDTO;
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
@Service
@RequiredArgsConstructor
public class SearchMapService implements SearchMapInterface {

    private final SearchMapRepository searchMapRepository;

    /**
     * @author hyo298, 김재효
     * @param searchMapRequestDTO 지도 요청 DTO
     * @return double[]
     */
    @Override
    public List<SearchMapResponseDTO> getMapList(SearchMapRequestDTO searchMapRequestDTO) {

        //좌표 조회 조건 설정시 지도 계산 처리
        if(searchMapRequestDTO.getLatitude() != null && searchMapRequestDTO.getLongitude() != null) {
            return getMapDistance(searchMapRequestDTO);
        }

        return searchMapRepository.findLocationMapDataList(searchMapRequestDTO);
    }

    private List<SearchMapResponseDTO> getMapDistance(SearchMapRequestDTO searchMapRequestDTO) {

        // step.1 : 조회조건 좌표를 기준으로 바운딩 박스 생성
        double latitude = searchMapRequestDTO.getLatitude();     //위도 - 조회 조건
        double longitude = searchMapRequestDTO.getLongitude();   //경도 - 조회 조건
        double[] calculateBoundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude);

        // step.2 : 바운딩 박스를 이용해서 조회
        searchMapRequestDTO.setMinLat(calculateBoundingBox[0]);
        searchMapRequestDTO.setMaxLat(calculateBoundingBox[1]);
        searchMapRequestDTO.setMinLon(calculateBoundingBox[2]);
        searchMapRequestDTO.setMaxLon(calculateBoundingBox[3]);
        List<SearchMapResponseDTO> getResponseList = searchMapRepository.findLocationMapDataList(searchMapRequestDTO);

        // step.3 : 조회된 데이터를 히버사인 공식을 이용하여 "기본거리표준" 에 의거해서 데이터 확보
        List<SearchMapResponseDTO> result = new LinkedList<>();
        for (SearchMapResponseDTO searchMapResponseDTO : getResponseList) {
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
