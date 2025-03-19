package com.snowroad.event.web;

import com.snowroad.event.service.EventService;
import com.snowroad.event.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "리스트 API", description = "리스트에서 사용하는 API")
public class EventController {

    private final EventService eventService;

    @Operation(summary="메인 배너 조회", description = "(이벤트) 메인 배너 컨텐츠를 조회합니다. ARG : ALL, 10, 20")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = HomeEventsResponseDto.class)))
    @GetMapping("/api/main-event/banner/{eventTypeCd}")
    public List<HomeEventsResponseDto> getMainBannerList(@RequestParam(defaultValue = "0") int page, @PathVariable String eventTypeCd) {
        List<HomeEventsResponseDto> events = eventService.getMainBannerList(eventTypeCd);
        return events;
    }

    @Operation(summary="메인 인기 리스트 조회", description = "(이벤트) 메인 팝업, 전시 순위 리스트를 조회합니다. ARG : ALL, 10, 20")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = HomeEventsResponseDto.class)))
    @GetMapping("/api/main-event/rank/{eventTypeCd}")
    public List<HomeEventsResponseDto> getMainRankList(@RequestParam(defaultValue = "0") int page, @PathVariable String eventTypeCd) {
        List<HomeEventsResponseDto> events = eventService.getMainRankList(eventTypeCd);
        return events;
    }


    @Operation(summary="메인 추천 리스트 조회", description = "(이벤트) 메인 팝업, 전시 리스트 추천 항목을 조회합니다. ARG : ALL, 10, 20")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = HomeEventsResponseDto.class)))
    @GetMapping("/api/main-events/rcmn/{eventTypeCd}")
    public List<HomeEventsResponseDto> getMainRecsList(@PathVariable String eventTypeCd){
        List<HomeEventsResponseDto> events = eventService.getMainRcmnList(eventTypeCd);
        return events;
    }

    @Operation(summary="메인 오픈임박 리스트 조회", description = "(이벤트) 메인 팝업, 전시 오픈임박 리스트를 조회합니다. eventTypeCd : ALL, 10, 20")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = HomeEventsResponseDto.class)))
    @GetMapping("/api/main-events/operStat/{eventTypeCd}")
    public PagedResponseDto<HomeEventsResponseDto> getMainOperStatList(@RequestParam(defaultValue = "0") int page, @PathVariable String eventTypeCd) {
        List<HomeEventsResponseDto> events = eventService.getMainOperStatList(eventTypeCd);
        return new PagedResponseDto<>(events, 10);
    }

    @Operation(summary="메인 마감임박 리스트 조회", description = "(이벤트) 메인 팝업, 전시 마감임박 리스트를 조회합니다. eventTypeCd : ALL, 10, 20")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = HomeEventsResponseDto.class)))
    @GetMapping("/api/main-events/operEnd/{eventTypeCd}")
    public PagedResponseDto<HomeEventsResponseDto> getMainOperEndList(@RequestParam(defaultValue = "0") int page, @PathVariable String eventTypeCd) {
        List<HomeEventsResponseDto> events = eventService.getMainOperEndList(eventTypeCd);
        return new PagedResponseDto<>(events, 10);
    }

    @Operation(summary="리스트 팝업/전시 조회", description = "(이벤트) 리스트페이지 등록된 팝업, 전시 리스트를 조회합니다.<br>" +
            "sortType-10:조회순 20:최신순 30:마감순 40/50:미완<br>  " +
            "ctgyId-category enum 내부 코드(FANDB,DESIGN 등)<br>  " +
            " geo-미완<br>" +
            "eventTypeCd : ALL, 10, 20")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = HomeEventsResponseDto.class)))
    @GetMapping("/api/events/list/{eventTypeCd}")
    public ResponseEntity<PagedResponseDto<DetailEventsResponseDto>> getEventList(
            @RequestParam(defaultValue = "0") int page, @PathVariable String eventTypeCd,
            @RequestParam(defaultValue = "20") String sortType,
            @RequestParam(required = false) List<String> ctgyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) List<String> geo

    ) {
        // 페이지네이션 반환
    //    Page<DetailEventsResponseDto> response = eventService.getEvntList(page, eventTypeCd, sortType, ctgyId, fromDate, toDate, geo);
        return ResponseEntity.ok(eventService.getEvntList(page, eventTypeCd, sortType, ctgyId, fromDate, toDate, geo));
    }

    @Operation(summary="상세 컨텐츠 조회", description = "(이벤트) 상세페이지 개별 팝업/전시 항목을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = EventContentsResponseDto.class)))
    @GetMapping("/api/events/cntn/{eventId}")
    public EventContentsResponseDto getEvnt(@PathVariable Long eventId) {
        // 개별 event 반환
        EventContentsResponseDto events = eventService.findEvntData(eventId);
        return events;
    }

}
