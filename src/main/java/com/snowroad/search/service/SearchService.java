package com.snowroad.search.service;

import com.snowroad.MorphemeAnalyzer.event.domain.EventMorphemeAnalyzerDTO;
import com.snowroad.MorphemeAnalyzer.event.domain.EventMorphemeAnalyzerRepository;
import com.snowroad.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import com.snowroad.entity.Events;
import com.snowroad.search.repository.SearchRepository;
import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.enums.SearchMapEnum;
import com.snowroad.search.interfaces.SearchMapInterface;
import com.snowroad.search.util.BoundingBoxCalculator;
import com.snowroad.search.util.HaversineFormula;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * 검색 서비스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-03-05
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService implements SearchMapInterface {

    private final SearchRepository searchRepository;
    private final KomoranAnalyzerInterface komoranAnalyzerInterface;
    private final EventMorphemeAnalyzerRepository eventMorphemeAnalyzerRepository;

    /**
     * @author hyo298, 김재효
     * @param searchRequestDTO 검색 요청 DTO
     * @return double[]
     */
    @Override
    public List<Events> getEvents(SearchRequestDTO searchRequestDTO) {

        List<Events> eventsList;

        // step.1 : 텍스트가 있는 경우 텍스트 검색을 진행하여 in 절을 구성합니다
        if(searchRequestDTO.getKeyword() != null
            && !searchRequestDTO.getKeyword().trim().isEmpty()) {
            searchRequestDTO.setEventIds(getKeywordSearchResult(searchRequestDTO));
        }

        // step.2 : 좌표가 있는 경우 지도 계산을 진행합니다.
        if(searchRequestDTO.getLatitude() != null && searchRequestDTO.getLongitude() != null) {
            eventsList = getMapDistance(searchRequestDTO);
        } else {
            eventsList = searchRepository.findLocationMapDataList(searchRequestDTO);
        }

        return eventsList;
    }

    private List<Events> getMapDistance(SearchRequestDTO searchRequestDTO) {

        // step.1 : 조회조건 좌표를 기준으로 바운딩 박스 생성
        double latitude = searchRequestDTO.getLatitude();     //위도 - 조회 조건
        double longitude = searchRequestDTO.getLongitude();   //경도 - 조회 조건
        double[] calculateBoundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude);

        // step.2 : 바운딩 박스를 이용해서 조회
        searchRequestDTO.setMinLat(calculateBoundingBox[0]);
        searchRequestDTO.setMaxLat(calculateBoundingBox[1]);
        searchRequestDTO.setMinLon(calculateBoundingBox[2]);
        searchRequestDTO.setMaxLon(calculateBoundingBox[3]);
        List<Events> getResponseList = searchRepository.findLocationMapDataList(searchRequestDTO);

        // step.3 : 조회된 데이터를 히버사인 공식을 이용하여 "기본거리표준" 에 의거해서 데이터 확보
        List<Events> result = new LinkedList<>();
        for (Events events : getResponseList) {
            double distance = HaversineFormula.calculateDistance(
                    latitude, longitude, events.getAddrLttd(), events.getAddrLotd()
            );
            if (distance <= SearchMapEnum.MAP_DISTANCE_STANDARD.getRate()) {
                log.info("""
                                
                                ========== START LOG ==========
                                거리 값: {}
                                이벤트 명: {}
                                ========== END LOG ==========""",
                        distance, events.getEventNm());
                result.add(events);
            }
        }
        return result;
    }

    private List<Long> getKeywordSearchResult(SearchRequestDTO searchRequestDTO) {
        Map<String, List<KomoranDTO>> komoranMap = komoranAnalyzerInterface.komoranAnalyzerMap(searchRequestDTO.getKeyword());

        List<EventMorphemeAnalyzerDTO> analyzerList = komoranMap.values().stream()
                .flatMap(List::stream)
                .peek(komoranDTO -> log.info("Token :: ' {} '  Pos [ code : {} | name : {} ]",
                        komoranDTO.getToken(),
                        komoranDTO.getPosCode(),
                        komoranDTO.getPosName()))
                .map(komoranDTO -> eventMorphemeAnalyzerRepository.findByMranCntn(komoranDTO.getToken()))
                .flatMap(List::stream)
                .toList();

        return analyzerList.stream()
                .map(EventMorphemeAnalyzerDTO::getEvntId)
                .distinct() // 중복 제거
                .toList();
    }
}
