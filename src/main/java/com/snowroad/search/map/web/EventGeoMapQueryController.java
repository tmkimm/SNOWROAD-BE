package com.snowroad.search.map.web;

import com.snowroad.search.map.dto.EventsGeoMapDto;
import com.snowroad.search.map.interfaces.EventGeoMapQueryInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Map 커스텀 마커 조회", description = "Map 커스텀 마커 구성시 사용하는 API")
public class EventGeoMapQueryController {

    private final EventGeoMapQueryInterface eventGeoMapQueryInterface;

    @Operation(summary="이벤트 커스텀 마커 조회", description = "등록된 이벤트를 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsGeoMapDto.class)))
    @PostMapping("/api/search/events")
    List<EventsGeoMapDto> getEventsGeoMapList(@RequestParam(defaultValue = "37.5540219315164") double addrLttd
            , @RequestParam(defaultValue = "126.922884921727") double addrLotd) {
        List<EventsGeoMapDto> list = eventGeoMapQueryInterface.getMapList(addrLttd, addrLotd);
        log.info("getEventsGeoMapList {} ",list);
       return list;
    }
}
