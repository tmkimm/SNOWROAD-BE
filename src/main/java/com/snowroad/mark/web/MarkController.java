package com.snowroad.mark.web;

import com.snowroad.event.web.dto.DetailEventsResponseDto;
import com.snowroad.event.web.dto.EventsListResponseDto;
import com.snowroad.event.web.dto.PagedResponseDto;
import com.snowroad.mark.service.MarkService;
import com.snowroad.mark.web.dto.*;
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

    @Operation(summary="[작업중] (신규) 팝업, 전시 즐겨찾기 등록", description = "[작성중] (즐겨찾기) 좋아요를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "즐겨찾기 등록 성공", content = @Content(schema = @Schema(implementation = MarkSaveRequestDto.class)))
    @PostMapping("/api/mark/event/insert")
    public Long saveMarkEvent(@RequestBody MarkSaveRequestDto requestDto) {
        return markService.saveMarkEvent(requestDto);
    }

    @Operation(summary="[작업중] (기존) 팝업, 전시 즐겨찾기 수정", description = "[작성중] (즐겨찾기) 좋아요를 재등록/해제 합니다.")
    @PutMapping("/api/mark/event/update")
    public Long update(@PathVariable Long id, @PathVariable Long evntId,
                       @RequestBody MarkSaveRequestDto requestDto) {
        return markService.update(id, evntId, requestDto);
    }

    @Operation(summary="즐겨찾기 팝업/전시  리스트 조회", description = "(즐겨찾기) 사용자가 즐겨찾기한 팝업, 전시 리스트를 조회합니다. (userId : 0001 )")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/mark/event/{userId}")
    public PagedResponseDto<MarkedEventResponseDTO> getEventList(@RequestParam(defaultValue = "0") int page, @PathVariable Long userId) {

        List<MarkedEventResponseDTO> events = markService.getMarkedEventList(userId);
        // PagedResponseDto로 반환
        return new PagedResponseDto<>(events, 9);
    }
}
