package com.snowroad.search.list.service;

import com.snowroad.MorphemeAnalyzer.event.domain.EventMorphemeAnalyzerDTO;
import com.snowroad.MorphemeAnalyzer.event.domain.EventMorphemeAnalyzerRepository;
import com.snowroad.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import com.snowroad.search.list.domain.SearchListRepository;
import com.snowroad.search.list.domain.SearchListResponseDTO;
import com.snowroad.search.list.interfaces.SearchListInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * 검색 서비스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-14
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchListService implements SearchListInterface {

    private final KomoranAnalyzerInterface komoranAnalyzerInterface;
    private final EventMorphemeAnalyzerRepository eventMorphemeAnalyzerRepository;
    private final SearchListRepository searchListRepository;

    @Override
    public List<SearchListResponseDTO> searchText(String keyword) {
        Map<String, List<KomoranDTO>> komoranMap = komoranAnalyzerInterface.komoranAnalyzerMap(keyword);

        List<EventMorphemeAnalyzerDTO> analyzerList = komoranMap.values().stream()
                .flatMap(List::stream)
                .peek(komoranDTO -> log.info("Token :: ' {} '  Pos [ code : {} | name : {} ]",
                        komoranDTO.getToken(),
                        komoranDTO.getPosCode(),
                        komoranDTO.getPosName()))
                .map(komoranDTO -> eventMorphemeAnalyzerRepository.findByMranCntn(komoranDTO.getToken()))
                .flatMap(List::stream)
                .toList();

        List<SearchListResponseDTO> finalResult = analyzerList.stream()
                .map(analyzerDTO -> searchListRepository.findByEvntId(analyzerDTO.getEvntId()))
                .flatMap(List::stream)
                .distinct()
                .toList();

        finalResult.forEach(dto -> log.info("Return DTO :: {}", dto));
        return finalResult;
    }

}
