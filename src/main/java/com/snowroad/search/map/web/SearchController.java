package com.snowroad.search.map.web;

import com.snowroad.entity.Events;
import com.snowroad.search.map.dto.SearchRequestDTO;
import com.snowroad.search.map.interfaces.SearchMapInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 지도 검색 컨트롤러
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-28
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Map API", description = "지도 처리 API")
public class SearchController {

    private final SearchMapInterface searchMapInterface;

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
    List<Events> getEvents(
            @Valid
            @ModelAttribute SearchRequestDTO searchRequestDTO) {
       return searchMapInterface.getEvents(searchRequestDTO);
    }
}
