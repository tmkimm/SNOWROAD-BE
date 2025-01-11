package com.snowroad.search.map.web;

import com.snowroad.search.map.dto.SearchMapResponseDTO;
import com.snowroad.search.map.interfaces.SearchMapInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 지도 검색 컨트롤러
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-09
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Map API", description = "지도 처리 API")
public class SearchMapController {

    private final SearchMapInterface searchMapInterface;

    /**
     *
     * 지도에서 커스텀 마커 구현을 위한 조회
     *
     * @author hyo298, 김재효
     * @param latitude 위도
     * @param longitude 경도
     * @return List
     */
    @Operation(summary="지도에서 이벤트 커스텀 마커 조회", description = "기본거리표준에 따라서 이벤트를 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = SearchMapResponseDTO.class)))
    @GetMapping("/api/search/events")
    List<SearchMapResponseDTO> getEventGeoMapList(@RequestParam(defaultValue = "37.5540219315164") double latitude
            , @RequestParam(defaultValue = "126.922884921727") double longitude) {
       return searchMapInterface.getMapList(latitude, longitude);
    }
}
