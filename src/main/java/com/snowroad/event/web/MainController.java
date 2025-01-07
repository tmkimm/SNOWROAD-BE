package com.snowroad.event.web;

import com.snowroad.user.web.dto.UserEventsListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RestController
@Tag(name = "메인 API", description = "메인화면에서 사용하는 API")
public class MainController {

//    private final MainService mainService;

    @Operation(summary="팝업, 전시 메인 순위 리스트 조회", description = "(사용자) 항목별 메인 팝업, 전시 순위 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserEventsListResponseDto.class)))
    @GetMapping("/api/main/rank")
    public List<UserEventsListResponseDto> getMainRankList(@RequestParam(defaultValue = "0") int page) {
        System.out.println("/api/main/rank 메인 팝업/전시 순위 리스트 조회, getMainRankList in");
        // 10개의 더미 이벤트 데이터 생성
        List<UserEventsListResponseDto> events = IntStream.range(0, 10)
                .mapToObj(i -> new UserEventsListResponseDto(
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

        return events;

        //return mainService.getMainRankList();
    }

    @Operation(summary="팝업, 전시 메인 추천 리스트 조회", description = "(메인) 항목별 메인 팝업, 전시 추천 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserEventsListResponseDto.class)))
    @GetMapping("/api/main/rcmn/{cygyId}")
    public List<UserEventsListResponseDto> getMainRecoList(@PathVariable String cygyId, @RequestParam(defaultValue = "0") int page) {
        System.out.println("/api/main/rcmn 메인 팝업/전시 추천항목 조회, getMainRcmn in");
        // 10개의 더미 이벤트 데이터 생성
        List<UserEventsListResponseDto> events = IntStream.range(0, 10)
                .mapToObj(i -> new UserEventsListResponseDto(
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
    }

    @Operation(summary="팝업, 전시 메인 오픈임박 조회", description = "(사용자) 항목별 메인 팝업, 전시 오픈임박 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserEventsListResponseDto.class)))
    @GetMapping("/api/main/operStat")
    public List<UserEventsListResponseDto> getMainOperStatList(@RequestParam(defaultValue = "0") int page) {
        System.out.println("/api/main/operStat 메인 팝업/전시 오픈임박 리스트 조회, getMainOperStatList in");
        // 10개의 더미 이벤트 데이터 생성
        List<UserEventsListResponseDto> events = IntStream.range(0, 10)
                .mapToObj(i -> new UserEventsListResponseDto(
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

        return events;

        //return mainService.getMainOperStatList();
    }


    @Operation(summary="팝업, 전시 메인 마감임박 조회", description = "(사용자) 항목별 메인 팝업, 전시 마감임박 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserEventsListResponseDto.class)))
    @GetMapping("/api/main/operEnd")
    public List<UserEventsListResponseDto> getMainOperEndList(@RequestParam(defaultValue = "0") int page) {
        System.out.println("/api/main/operEnd 메인 팝업/전시 마감임박 리스트 조회, getMainOperEndList in");
        // 10개의 더미 이벤트 데이터 생성
        List<UserEventsListResponseDto> events = IntStream.range(0, 10)
                .mapToObj(i -> new UserEventsListResponseDto(
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

        return events;

        //return mainService.getMainOperEndList();
    }

}
