package com.snowroad.web;

import com.snowroad.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Tag(name = "어드민 API", description = "어드민에서 사용하는 API")
public class AdminController {
    @Operation(summary="어드민 로그인", description = "(관리자) 어드민 페이지에 로그인합니다.\n로그인 성공 시 쿠키에 Refresh token, Access token이 저장됩니다.")
    //@ApiResponse(responseCode = "200", description = "수신함 조회 성공", content = @Content(schema = @Schema(implementation = AlarmReceiveDto.class)))
    @PostMapping("/api/admin/login")
    public void login(@RequestBody AdminLoginRequestDTO requestDto) {

    }

    @Operation(summary="팝업, 전시 리스트 조회", description = "(관리자) 등록된 팝업, 전시 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EventsListResponseDto.class)))
    @GetMapping("/api/admin/events")
    public void getList() {

    }
    @Operation(summary="팝업, 전시 상세 조회", description = "(관리자) 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = EventsResponseDto.class)))
    @GetMapping("/api/admin//events/{eventId}")
    public void get() {

    }

    @Operation(summary="팝업, 전시 등록", description = "(관리자) 이벤트를 등록합니다.")
    //@ApiResponse(responseCode = "200", description = "수신함 조회 성공", content = @Content(schema = @Schema(implementation = AlarmReceiveDto.class)))
    @PostMapping("/api/admin/events")
    public void save(@RequestBody EventsSaveRequestDto requestDto) {

    }
    @Operation(
            summary = "팝업, 전시 이미지 등록",
            description = "(관리자) 이벤트 이미지를 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = EventsFileUploadRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "파일 업로드 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("/api/admin/events/{eventId}/files")
    public void uploadFiles(
            @PathVariable String eventId,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("mainImage") MultipartFile mainImage) {
        // 업로드 처리 로직
        if (mainImage != null) {
            System.out.println("대표 이미지: " + mainImage.getOriginalFilename());
        }
        for (MultipartFile file : files) {
            System.out.println("업로드된 파일: " + file.getOriginalFilename());
        }
    }

    @Operation(summary="팝업, 전시 삭제", description = "(관리자) 이벤트를 삭제합니다.")
    @DeleteMapping("/api/admin/events/{eventId}")
    public void delete(@PathVariable String eventId) {

    }
}
