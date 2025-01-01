package com.snowroad.web;

import com.snowroad.service.AdminService;
import com.snowroad.service.EventService;
import com.snowroad.web.dto.*;
import com.snowroad.web.util.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RestController
@Tag(name = "리스트 API", description = "리스트에서 사용하는 API")
public class ListController {

 //   private final ListService listService;

    @Operation(summary="팝업, 전시 리스트 조회", description = "(사용자) 등록된 팝업, 전시 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserEventsListResponseDto.class)))
    @GetMapping("/api/user/events")
    public List<UserEventsListResponseDto> getList(@RequestParam(defaultValue = "0") int page) {
        System.out.println("/api/user/events 팝업, 전시 리스트 조회, getList in");
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
                        200,    // viewNmvl
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());

        return events;

        //return listService.findAllDesc();
    }


    @Operation(summary="팝업, 전시 상세항목 조회", description = "(사용자) 상세 팝업/전시 항목을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = UserEventsListResponseDto.class)))
    @GetMapping("/api/user/events/{id}")
    public UserEventsListResponseDto findById(@PathVariable Long id) {
        System.out.println("/api/user/events/{id} 팝업, 전시 리스트 상세 조회, findById in");
        UserEventsListResponseDto dto = new UserEventsListResponseDto(
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
        return dto;

        //return listService.findById(id);
    }

    @Operation(summary="팝업, 전시 리스트 추천", description = "(사용자) 팝업, 전시 리스트 추천 항목을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserEventsListResponseDto.class)))
    @GetMapping("/api/user/random/{ppstTypeCd}")
    public List<UserEventsListResponseDto> getRandomList(@PathVariable String ppstTypeCd){
        System.out.println("/api/user/events 팝업, 전시 리스트 랜덤조회, getRandomList in");
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
                        200,    // viewNmvl
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());

        return events;

        //return listService.findAllDesc();
    }


}
