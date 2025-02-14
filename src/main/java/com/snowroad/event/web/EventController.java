package com.snowroad.event.web;

import com.snowroad.event.service.EventService;
import com.snowroad.event.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RestController
@Tag(name = "리스트 API", description = "리스트에서 사용하는 API")
public class EventController {

    private final EventService eventService;

    @Operation(summary="메인 인기 리스트 조회", description = "(이벤트) 메인 팝업, 전시 순위 리스트를 조회합니다. ARG : all, PPST, ENBN")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = HomeEventsResponseDto.class)))
    @GetMapping("/api/event/rank/{eventTypeCd}")
    public List<HomeEventsResponseDto> getMainRankList(@RequestParam(defaultValue = "0") int page, @PathVariable String eventTypeCd) {
        List<HomeEventsResponseDto> events = eventService.getMainRankList(eventTypeCd);
        return events;
    }


    @Operation(summary="메인 추천 리스트 조회", description = "(이벤트) 메인 팝업, 전시 리스트 추천 항목을 조회합니다. ARG : all, PPST, ENBN")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/events/rcmn/{eventTypeCd}")
    public List<HomeEventsResponseDto> getMainRecsList(@PathVariable String eventTypeCd){
        List<HomeEventsResponseDto> events = eventService.getMainRcmnList(eventTypeCd);
        return events;
    }

    // 250109 상단 '팝업, 전시 리스트 추천'으로 대체
/*    @Operation(summary="팝업, 전시 메인 추천 리스트 조회", description = "(이벤트) 메인 팝업, 전시 추천 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/main/rcmn/{cygyId}")
    public List<EventsListResponseDto> getMainRecoList(@PathVariable String cygyId, @RequestParam(defaultValue = "0") int page) {
        // 10개의 더미 이벤트 데이터 생성
        List<EventsListResponseDto> events = IntStream.range(0, 10)
                .mapToObj(i -> new EventsListResponseDto(
                        (long) (i + 1),                               // eventId
                        "이벤트 " + (i + 1),                         // eventNm
                        "이벤트 " + (i + 1) + "에 대한 설명",        // eventCntn
                        "서울시 성동구 성수동1가 777",                // eventAddr
                        "20250101",                                 // operStatDt
                        "20250131",                                 // operEndDt
                        "10:00 ~ 18:00",                            // operDttmCntn
                        cygyId,                        // ctgyId (CTG1, CTG2, CTG3)
                        "패션",                        // ctgyNm
                        "ppst",                         // ppstEnbnTypeCd (PPS1, PPS2)
                        "팝업",                         // ppstEnbnTypeNm (PPS1, PPS2)
                        "Y",                            //likeYn
                        200,    // viewNmvl ** 현재 db상 일자별 count가 아닌 전체 count인데..이부분 ask
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());

        return events;

        //return mainService.getMainRecoList();
    }*/

    @Operation(summary="메인 오픈임박 리스트 조회", description = "(이벤트) 메인 팝업, 전시 오픈임박 리스트를 조회합니다. eventTypeCd : PPST, ENBN")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = HomeEventsResponseDto.class)))
    @GetMapping("/api/events/operStat/{eventTypeCd}")
    public PagedResponseDto<HomeEventsResponseDto> getMainOperStatList(@RequestParam(defaultValue = "0") int page, @PathVariable String eventTypeCd) {
        List<HomeEventsResponseDto> events = eventService.getMainOperStatList(eventTypeCd);
        return new PagedResponseDto<>(events, 10);
    }

    @Operation(summary="메인 마감임박 리스트 조회", description = "(이벤트) 메인 팝업, 전시 마감임박 리스트를 조회합니다. eventTypeCd : PPST, ENBN")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = HomeEventsResponseDto.class)))
    @GetMapping("/api/events/operEnd/{eventTypeCd}")
    public PagedResponseDto<HomeEventsResponseDto> getMainOperEndList(@RequestParam(defaultValue = "0") int page, @PathVariable String eventTypeCd) {
        List<HomeEventsResponseDto> events = eventService.getMainOperEndList(eventTypeCd);
        return new PagedResponseDto<>(events, 10);
    }

    @Operation(summary="즐겨찾기 팝업/전시 리스트 조회", description = "(이벤트) 즐겨찾기된 팝업, 전시 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = DetailEventsResponseDto.class)))
    @GetMapping("/api/events/marked")
    public PagedResponseDto<DetailEventsResponseDto> getMarkedList(@RequestParam(defaultValue = "0") int page) {
        List<DetailEventsResponseDto> events = eventService.findEvntMarkedList();
        // PagedResponseDto로 반환
        return new PagedResponseDto<>(events, 11);
    }


    @Operation(summary="리스트 팝업/전시 조회", description = "[작업중](이벤트) 리스트페이지 등록된 팝업, 전시 리스트를 조회합니다. (sortType : 10, 20, 30 그외 항목 구현중)")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/events/list/{sortType}")
    public PagedResponseDto<DetailEventsResponseDto> getEventList(@RequestParam(defaultValue = "0") int page, @PathVariable String sortType) {

        List<DetailEventsResponseDto> events = eventService.getEvntList(sortType);
        // PagedResponseDto로 반환
        return new PagedResponseDto<>(events, 11);
    }


    @Operation(summary="상세 컨텐츠 조회", description = "(이벤트) 상세페이지 개별 팝업/전시 항목을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/events/evnt/{id}")
    public EventsResponseDto getEvnt(@PathVariable Long id) {
        // 개별 event 반환
        EventsResponseDto events = eventService.findEvntData(id);
        return events;
    }

}
