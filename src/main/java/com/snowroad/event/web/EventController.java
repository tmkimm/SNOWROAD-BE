package com.snowroad.event.web;

import com.snowroad.event.service.EventService;
import com.snowroad.event.web.dto.PagedResponseDto;
import com.snowroad.event.web.dto.EventsResponseDto;
import com.snowroad.event.web.dto.EventsListResponseDto;
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

    @Operation(summary="팝업, 전시 리스트 조회", description = "(이벤트) 등록된 팝업, 전시 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/events/list")
    public PagedResponseDto<EventsListResponseDto> getEventList(@RequestParam(defaultValue = "0") int page) {
        // 10개의 더미 이벤트 데이터 생성
/*        List<EventsListResponseDto> events = IntStream.range(0, 10)
                .mapToObj(i -> new EventsListResponseDto(
                        (long) (i + 1),                               // eventId
                        "이벤트 " + (i + 1),                         // eventNm
                        "이벤트 " + (i + 1) + "에 대한 설명",        // eventCntn
                        "서울시 성동구 성수동1가 777",                // eventAddr
                        "20250101",                                 // operStatDt
                        "20250131",                                 // operEndDt
                        "10:00 ~ 18:00",                            // operDttmCntn
                        "CTG" + (i % 3 + 1),                        // ctgyId (CTG1, CTG2, CTG3)
                        "패션",                        // ctgyNm
                        "ppst",                         // ppstEnbnTypeCd (PPS1, PPS2)
                        "팝업",                         // ppstEnbnTypeNm (PPS1, PPS2)
                        "Y",                            //likeYn
                        200,    // viewNmvl
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());*/

//        return events;
        List<EventsListResponseDto> events = eventService.findEvntList();

        // PagedResponseDto로 반환
        return new PagedResponseDto<>(events, 11);
    }


    @Operation(summary="팝업, 전시 상세항목 조회", description = "(이벤트) 상세 팝업/전시 항목을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/events/evnt/{id}")
    public EventsResponseDto getEvnt(@PathVariable Long id) {
/*        EventsListResponseDto dto = new EventsListResponseDto(
                id,                               // eventId
                "이벤트 " + (id),                         // eventNm
                "이벤트 " + (id) + "에 대한 설명",        // eventCntn
                "서울시 성동구 성수동1가 777",                // eventAddr
                "20250101",                                 // operStatDt
                "20250131",                                 // operEndDt
                "10:00 ~ 18:00",                            // operDttmCntn
                "CTG" + 1,                        // ctgyId (CTG1, CTG2, CTG3)
                "패션",                        // ctgyNm
                "ppst",                         // ppstEnbnTypeCd (PPS1, PPS2)
                "팝업",                         // ppstEnbnTypeNm (PPS1, PPS2)
                "Y",                            //likeYn
                200,    // viewNmvl
                "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
        );
        return dto;*/

        EventsResponseDto events = eventService.findEvntData(id);

        // 개별 event 반환
        return events;
    }

    @Operation(summary="팝업, 전시 리스트 추천", description = "(이벤트) 팝업, 전시 리스트 추천 항목을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/events/rcmn/{ppstTypeCd}")
    public List<EventsListResponseDto> getRandomList(@PathVariable String ppstTypeCd){
        System.out.println("팝업, 전시 리스트 랜덤조회, getRandomList in");
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
                        "CTG" + (i % 3 + 1),                        // ctgyId (CTG1, CTG2, CTG3)
                        "패션",                        // ctgyNm
                        "ppst",                         // ppstEnbnTypeCd (PPS1, PPS2)
                        "팝업",                         // ppstEnbnTypeNm (PPS1, PPS2)
                        "Y",                            //likeYn
                        200,    // viewNmvl
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());

        return events;

        //return listService.findAllDesc();
    }

    @Operation(summary="즐겨찾기 팝업/전시 리스트 조회", description = "(이벤트) 즐겨찾기된 팝업, 전시 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/events/mark")
    public PagedResponseDto<EventsListResponseDto> getMarkedList(@RequestParam(defaultValue = "0") int page) {

        // 5개의 더미 이벤트 데이터 생성
/*        List<EventsListResponseDto> events = IntStream.range(0, 5)
                .mapToObj(i -> new EventsListResponseDto(
                        (long) (i + 1),                               // eventId
                        "이벤트 " + (i + 1),                         // eventNm
                        "이벤트 " + (i + 1) + "에 대한 설명",        // eventCntn
                        "서울시 성동구 성수동1가 777",                // eventAddr
                        "20250101",                                 // operStatDt
                        "20250131",                                 // operEndDt
                        "10:00 ~ 18:00",                            // operDttmCntn
                        "CTG" + (i % 3 + 1),                        // ctgyId (CTG1, CTG2, CTG3)
                        "패션",                        // ctgyNm
                        "ppst",                         // ppstEnbnTypeCd (PPS1, PPS2)
                        "팝업",                         // ppstEnbnTypeNm (PPS1, PPS2)
                        "Y",                            //markYn
                        200,    // viewNmvl
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());

        return events;*/

        List<EventsListResponseDto> events = eventService.findEvntMarkedList();
        // PagedResponseDto로 반환
        return new PagedResponseDto<>(events, 11);
    }


    @Operation(summary="팝업, 전시 메인 순위 리스트 조회", description = "(이벤트) 메인 팝업, 전시 순위 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/event/rank")
    public PagedResponseDto<EventsListResponseDto> getMainRankList(@RequestParam(defaultValue = "0") int page) {
        System.out.println("/api/main/rank 메인 팝업/전시 순위 리스트 조회, getMainRankList in");
        // 10개의 더미 이벤트 데이터 생성
/*        List<EventsListResponseDto> events = IntStream.range(0, 10)
                .mapToObj(i -> new EventsListResponseDto(
                        (long) (i + 1),                               // eventId
                        "이벤트 " + (i + 1),                         // eventNm
                        "이벤트 " + (i + 1) + "에 대한 설명",        // eventCntn
                        "서울시 성동구 성수동1가 777",                // eventAddr
                        "20250101",                                 // operStatDt
                        "20250131",                                 // operEndDt
                        "10:00 ~ 18:00",                            // operDttmCntn
                        "CTG" + (i % 3 + 1),                        // ctgyId (CTG1, CTG2, CTG3)
                        "패션",                        // ctgyNm
                        "ppst",                         // ppstEnbnTypeCd (PPS1, PPS2)
                        "팝업",                         // ppstEnbnTypeNm (PPS1, PPS2)
                        "Y",                            //likeYn
                        200,    // viewNmvl ** 현재 db상 일자별 count가 아닌 전체 count인데..이부분 ask
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());*/

        // return events;
        List<EventsListResponseDto> events = eventService.getMainRankList();
        return new PagedResponseDto<>(events, 11);

    }

    // 250109 상단 '팝업, 전시 리스트 추천'으로 대체
/*    @Operation(summary="팝업, 전시 메인 추천 리스트 조회", description = "(이벤트) 메인 팝업, 전시 추천 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/main/rcmn/{cygyId}")
    public List<EventsListResponseDto> getMainRecoList(@PathVariable String cygyId, @RequestParam(defaultValue = "0") int page) {
        System.out.println("/api/main/rcmn 메인 팝업/전시 추천항목 조회, getMainRcmn in");
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

    @Operation(summary="팝업, 전시 메인 오픈임박 조회", description = "(이벤트) 메인 팝업, 전시 오픈임박 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/events/operStat")
    public PagedResponseDto<EventsListResponseDto> getMainOperStatList(@RequestParam(defaultValue = "0") int page) {
        System.out.println("/api/main/operStat 메인 팝업/전시 오픈임박 리스트 조회, getMainOperStatList in");
        // 10개의 더미 이벤트 데이터 생성
/*        List<EventsListResponseDto> events = IntStream.range(0, 10)
                .mapToObj(i -> new EventsListResponseDto(
                        (long) (i + 1),                               // eventId
                        "이벤트 " + (i + 1),                         // eventNm
                        "이벤트 " + (i + 1) + "에 대한 설명",        // eventCntn
                        "서울시 성동구 성수동1가 777",                // eventAddr
                        "20250101",                                 // operStatDt
                        "20250131",                                 // operEndDt
                        "10:00 ~ 18:00",                            // operDttmCntn
                        "CTG" + (i % 3 + 1),                        // ctgyId (CTG1, CTG2, CTG3)
                        "패션",                        // ctgyNm
                        "ppst",                         // ppstEnbnTypeCd (PPS1, PPS2)
                        "팝업",                         // ppstEnbnTypeNm (PPS1, PPS2)
                        "Y",                            //likeYn
                        200,    // viewNmvl ** 현재 db상 일자별 count가 아닌 전체 count인데..이부분 ask
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());

        return events;*/

        List<EventsListResponseDto> events = eventService.getMainOperStatList();
        return new PagedResponseDto<>(events, 11);
    }


    @Operation(summary="팝업, 전시 메인 마감임박 조회", description = "(이벤트) 메인 팝업, 전시 마감임박 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/events/operEnd")
    public PagedResponseDto<EventsListResponseDto> getMainOperEndList(@RequestParam(defaultValue = "0") int page) {

        // 10개의 더미 이벤트 데이터 생성
  /*      List<EventsListResponseDto> events = IntStream.range(0, 10)
                .mapToObj(i -> new EventsListResponseDto(
                        (long) (i + 1),                               // eventId
                        "이벤트 " + (i + 1),                         // eventNm
                        "이벤트 " + (i + 1) + "에 대한 설명",        // eventCntn
                        "서울시 성동구 성수동1가 777",                // eventAddr
                        "20250101",                                 // operStatDt
                        "20250131",                                 // operEndDt
                        "10:00 ~ 18:00",                            // operDttmCntn
                        "CTG" + (i % 3 + 1),                        // ctgyId (CTG1, CTG2, CTG3)
                        "패션",                        // ctgyNm
                        "ppst",                         // ppstEnbnTypeCd (PPS1, PPS2)
                        "팝업",                         // ppstEnbnTypeNm (PPS1, PPS2)
                        "Y",                            //likeYn
                        200,    // viewNmvl ** 현재 db상 일자별 count가 아닌 전체 count인데..이부분 ask
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());

        return events;*/

        List<EventsListResponseDto> events = eventService.getMainOperEndList();
        return new PagedResponseDto<>(events, 11);
    }

}
