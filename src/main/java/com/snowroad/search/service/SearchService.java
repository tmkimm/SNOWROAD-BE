package com.snowroad.search.service;

import com.snowroad.MorphemeAnalyzer.event.domain.EventMorphemeAnalyzerDTO;
import com.snowroad.MorphemeAnalyzer.event.domain.EventMorphemeAnalyzerRepository;
import com.snowroad.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import com.snowroad.entity.Events;
import com.snowroad.geodata.interfaces.GeoDataInterface;
import com.snowroad.search.dto.SearchPagedResponse;
import com.snowroad.search.repository.SearchRepository;
import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.interfaces.SearchInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 검색 및 리스트 조회 서비스
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
     *
     * 검색 조건 List 처리
     *
     * @author hyo298, 김재효
     * @param searchRequestDTO 검색 요청 DTO
     * @return List<Events>
     */
    @Override
    @Transactional(readOnly = true)
    public SearchPagedResponse getEvents(SearchRequestDTO searchRequestDTO) {

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
        // Text 검색과 거리표준에서 중복된 부분을 제거한다
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

        // step.4 : 검색 조건 설정
        searchRequestDTO.setEventIds(filteredEventIds);

        // step.5 : 정렬 조건 설정
        String sortType = searchRequestDTO.getSortType();
        Sort sort = Sort.by(Sort.Direction.DESC, "operStatDt");
        if(sortType != null && !sortType.trim().isEmpty()) {
            //break;
            sort = switch (searchRequestDTO.getSortType()) {
                case "인기순" -> throw new IllegalArgumentException("인기순_업데이트 예정");

                //sort = Sort.by(Sort.Direction.DESC, "viewNmvl");
                //break;
                case "최신순" -> Sort.by(Sort.Direction.DESC, "createdDate");
                case "마감순" -> Sort.by(Sort.Direction.DESC, "operEndDt");
                case "지역순" -> throw new IllegalArgumentException("지역순_업데이트 예정");

                //break;
                case "거리순" -> throw new IllegalArgumentException("거리순_업데이트 예정");
                default -> sort;
            };
        }

        // step.6 : 데이터 조회
        Pageable pageable = PageRequest.of(searchRequestDTO.getPage(), 12, sort);
        Page<Events> events = searchRepository.findSearchEventDataList(pageable, searchRequestDTO);

        // step.7 : 반환
        // 데이터, 전체 페이지수, 전체 데이터 건수
        return new SearchPagedResponse(
                events.getContent(),
                events.getTotalPages(),
                events.getTotalElements()
        );
    }

    /**
     *
     * 거리를 계산을 수행한다.
     *
     * @author hyo298, 김재효
     * @param searchRequestDTO 검색 요청 DTO
     * @return List<Long>
     */
    private List<Long> getMapDistance(SearchRequestDTO searchRequestDTO) {
        double latitude = searchRequestDTO.getLatitude();     //위도 - 조회 조건
        double longitude = searchRequestDTO.getLongitude();   //경도 - 조회 조건
        List<Events> geoDataList = geoDataInterface.getGeoDataByLocation(latitude, longitude);
        return geoDataList.stream()
                .map(Events::getEventId)
                .distinct() // 중복 제거
                .toList();
    }

    /**
     *
     * Text 검색을 수행한다.
     *
     * @author hyo298, 김재효
     * @param searchRequestDTO 검색 요청 DTO
     * @return List<Long>
     */
    private List<Long> getKeywordSearchResult(SearchRequestDTO searchRequestDTO) {
        Map<String, List<KomoranDTO>> komoranMap = komoranAnalyzerInterface.komoranAnalyzerMap(searchRequestDTO.getKeyword());

        // step.1 : 검색
        // 체언 형태분석 데이터에서 검색어를 찾는다.
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
