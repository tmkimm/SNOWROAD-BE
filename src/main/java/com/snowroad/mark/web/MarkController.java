package com.snowroad.mark.web;

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

    @Operation(summary="(신규) 팝업, 전시 즐겨찾기 등록", description = "(즐겨찾기) 좋아요를 등록합니다.")
    //@ApiResponse(responseCode = "200", description = "수신함 조회 성공", content = @Content(schema = @Schema(implementation = AlarmReceiveDto.class)))
    @PostMapping("/api/mark/marks")
    public Long save(@RequestBody MarkSaveRequestDto requestDto) {
        return markService.save(requestDto);
    }

    @Operation(summary="(기존) 팝업, 전시 즐겨찾기 수정", description = "(즐겨찾기) 좋아요를 재등록/해제 합니다.")
    @PutMapping("/api/mark/marks/{id}")
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
