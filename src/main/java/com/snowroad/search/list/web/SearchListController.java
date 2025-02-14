package com.snowroad.search.list.web;

import com.snowroad.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *
 * 목록 검색 컨트롤러
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-15
 *
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "검색 API", description = "검색 처리 API")
public class SearchListController {

    private final KomoranAnalyzerInterface komoranAnalyzerInterface;

    /**
     *
     * @author hyo298, 김재효
     * @param searchText 검색어
     * @return List
     */
    @Operation(summary="목록 검색", description = "검색합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/search/list")
    List<String> getSearchList(@RequestParam String searchText) {
        Map<String, List<KomoranDTO>> komoranMap = komoranAnalyzerInterface.komoranAnalyzerMap(searchText);
        return null;
    }
}
