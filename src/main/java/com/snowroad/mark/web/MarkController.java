package com.snowroad.mark.web;

import com.snowroad.common.exception.UnauthorizedException;
import com.snowroad.common.util.CurrentUser;
import com.snowroad.config.auth.dto.CustomUserDetails;
import com.snowroad.event.web.dto.PagedResponseDto;
import com.snowroad.mark.service.MarkService;
import com.snowroad.mark.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mark-events")
@Tag(name = "사용자 관심 팝업/전시 API", description = "사용자의 관심 팝업/전시 API")
public class MarkController {

    private final MarkService markService;

    @Operation(summary="관심 팝업/전시 리스트 조회", description = "(관심) 사용자가 즐겨찾기한 팝업, 전시 리스트를 조회합니다. (테스트 userId:11)")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = MarkedEventResponseDTO.class)))
    @GetMapping("/retrieve")
    public ResponseEntity<PagedResponseDto<MarkedEventResponseDTO>> getMarkedEventList(@RequestParam(defaultValue = "0") int page, @CurrentUser CustomUserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 존재하지 않습니다. 로그인이 필요합니다.");
        }

        return ResponseEntity.ok(markService.getMarkedEventList(page, userDetails.getUserId()));
    }

    @Operation(summary="관심 팝업/전시 즐겨찾기 추가/삭제", description = "(관심) 팝업/전시를 추가/삭제합니다. likeYn을 N으로 넘길시 관심 컨텐츠에서 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "즐겨찾기 추가 성공", content = @Content(schema = @Schema(implementation = MarkSaveRequestDto.class)))
    @PostMapping("/save")
    // TODO - 등록한 컨텐츠에 따라 response 분리?
    public ResponseEntity<String> addMarkEvent(@RequestBody MarkSaveRequestDto requestDto, @CurrentUser CustomUserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 존재하지 않습니다. 로그인이 필요합니다.");
        }

        markService.addMarkEvent(requestDto, userDetails.getUserId());

        // likeYn이 N일 경우는 삭제 처리된 것으로 판단하여 메시지 반환
        if ("N".equals(requestDto.getLikeYn())) {
            return ResponseEntity.ok("내 즐겨찾기에서 삭제되었습니다.");
        }
        else return ResponseEntity.ok("내 즐겨찾기에 추가되었습니다.");
    }

}
