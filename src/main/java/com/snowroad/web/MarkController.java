package com.snowroad.web;

import com.snowroad.service.MarkService;
import com.snowroad.web.dto.*;
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
@Tag(name = "즐겨찾기 API", description = "즐겨찾기 기능과 관련한 API")
public class MarkController {

    private final MarkService markService;

    @Operation(summary="즐겨찾기 팝업/전시 리스트 조회", description = "(즐겨찾기) 즐겨찾기된 팝업, 전시 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserEventsListResponseDto.class)))
    @GetMapping("/api/mark/markedList")
    public List<UserEventsListResponseDto> getList(@RequestParam(defaultValue = "0") int page) {

        // 5개의 더미 이벤트 데이터 생성
        List<UserEventsListResponseDto> events = IntStream.range(0, 5)
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
                        "Y",                            //markYn
                        200,    // viewNmvl
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg",
                        "https://cf.festa.io/img/2024-11-28/7d85d9f9-f041-4a53-b840-027abf836a80.jpg"
                ))
                .collect(Collectors.toList());

        return events;

        //return eventService.findAllDesc();
    }

    @Operation(summary="(신규) 팝업, 전시 즐겨찾기 등록", description = "(즐겨찾기) 좋아요를 등록합니다.")
    //@ApiResponse(responseCode = "200", description = "수신함 조회 성공", content = @Content(schema = @Schema(implementation = AlarmReceiveDto.class)))
    @PostMapping("/api/mark/marks")
    public Long save(@RequestBody MarkSaveRequestDto requestDto) {
        return markService.save(requestDto);
    }

    @Operation(summary="(기존) 팝업, 전시 즐겨찾기 수정", description = "(즐겨찾기) 좋아요를 재등록/해제 합니다.")
    @PutMapping("/api/mark/marks/{id}/{evntId}")
    public Long update(@PathVariable Long id, @PathVariable String evntId,
                       @RequestBody MarkSaveRequestDto requestDto) {
        return markService.update(id, evntId, requestDto);
    }


/*    @Operation(summary="팝업, 전시 좋아요 삭제", description = "(즐겨찾기) 좋아요를 해제(삭제)합니다.")
    @DeleteMapping("/api/mark/events/{eventId}")
    public Long delete(@PathVariable Long eventId) {
        markService.delete(eventId);
        return eventId;
    }*/
}
