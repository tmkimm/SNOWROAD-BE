package com.snowroad.search.list.web;

import com.snowroad.search.list.domain.SearchListResponseDTO;
import com.snowroad.search.list.interfaces.SearchListInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 검색 컨트롤러
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-14
 *
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "검색 API", description = "검색 처리 API")
public class SearchListController {

    private final SearchListInterface searchListInterface;

    /**
     *
     * @author hyo298, 김재효
     * @param searchText 검색어
     * @return List
     */
    @Operation(summary="목록 검색", description = "검색합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/search/list")
    List<SearchListResponseDTO> getSearchList(@RequestParam String searchText) {
        return searchListInterface.searchText(searchText);
    }
}
