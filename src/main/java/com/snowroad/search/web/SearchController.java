package com.snowroad.search.web;

import com.snowroad.entity.Events;
import com.snowroad.search.dto.SearchPagedResponse;
import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.interfaces.SearchInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 검색 컨트롤러
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-03-05
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "검색 API", description = "검색과 관련된 API")
public class SearchController {

    private final SearchInterface searchInterface;

    /**
     *
     * 이벤트 검색
     *
     * @author hyo298, 김재효
     * @param searchRequestDTO 이벤트 검색 DTO
     * @return List
     */
    @Operation(
        summary="이벤트 검색"
        , description = "설정된 조회 조건으로 이벤트를 조회합니다."
        , responses = {
            @ApiResponse(
                responseCode = "200"
                , description = "Map Custom Markers Search Successful"
                , content = @Content(schema = @Schema(implementation = Events.class))
            )
            , @ApiResponse(
                responseCode = "500"
                , description = "Server Error"
                , content = @Content()
            )
        }
    )
    @GetMapping(value = "/api/search/events")
    public ResponseEntity<SearchPagedResponse> getEvents(
            @Parameter(description = "검색")
            @Valid
            @ParameterObject SearchRequestDTO searchRequestDTO) {
       return ResponseEntity.ok(searchInterface.getEvents(searchRequestDTO));
    }
}
