package com.snowroad.search.service;

import com.snowroad.MorphemeAnalyzer.event.domain.EventMorphemeAnalyzerDTO;
import com.snowroad.MorphemeAnalyzer.event.domain.EventMorphemeAnalyzerRepository;
import com.snowroad.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import com.snowroad.entity.Events;
import com.snowroad.geodata.interfaces.GeoDataInterface;
import com.snowroad.search.repository.SearchRepository;
import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.interfaces.SearchInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 검색 서비스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-04-18
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService implements SearchInterface {

    private final SearchRepository searchRepository;
    private final KomoranAnalyzerInterface komoranAnalyzerInterface;
    private final EventMorphemeAnalyzerRepository eventMorphemeAnalyzerRepository;
    private final GeoDataInterface geoDataInterface;

    /**
     * @author hyo298, 김재효
     * @param searchRequestDTO 검색 요청 DTO
     * @return List<Events>
     */
    @Override
    public List<Events> getEvents(SearchRequestDTO searchRequestDTO) {
        // step.1 : Text 검색
        List<Long> keywordEventIds = null;
        if(searchRequestDTO.getKeyword() != null && !searchRequestDTO.getKeyword().trim().isEmpty()) {
            keywordEventIds = getKeywordSearchResult(searchRequestDTO);
        }

        // step.2 : 거리 표준
        List<Long> locationEventIds = null;
        if(searchRequestDTO.getLatitude() != null && searchRequestDTO.getLongitude() != null) {
            locationEventIds = getMapDistance(searchRequestDTO);
        }

        // step.3 : 교집합 구성
        List<Long> filteredEventIds = null;
        if (keywordEventIds != null && locationEventIds != null) {
            filteredEventIds = keywordEventIds.stream()
                    .filter(locationEventIds::contains)
                    .collect(Collectors.toList());
        } else if (keywordEventIds != null) {
            filteredEventIds = keywordEventIds;
        } else if (locationEventIds != null) {
            filteredEventIds = locationEventIds;
        }

        // step.4 : 검색
        searchRequestDTO.setEventIds(filteredEventIds);
        return searchRepository.findLocationMapDataList(searchRequestDTO);
    }

    private List<Long> getMapDistance(SearchRequestDTO searchRequestDTO) {
        double latitude = searchRequestDTO.getLatitude();     //위도 - 조회 조건
        double longitude = searchRequestDTO.getLongitude();   //경도 - 조회 조건
        List<Events> geoDataList = geoDataInterface.getGeoDataByLocation(latitude, longitude);
        return geoDataList.stream()
                .map(Events::getEventId)
                .distinct() // 중복 제거
                .toList();
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
