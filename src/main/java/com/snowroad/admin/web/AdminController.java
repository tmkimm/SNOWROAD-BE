package com.snowroad.admin.web;

import com.snowroad.admin.web.dto.AdminLoginRequestDTO;
import com.snowroad.admin.web.dto.AdminLoginResponseDTO;
import com.snowroad.common.util.CurrentUser;
import com.snowroad.entity.Events;
import com.snowroad.event.web.dto.EventsResponseDto;
import com.snowroad.event.web.dto.EventsSaveRequestDto;
import com.snowroad.event.web.dto.PagedResponseDto;
import com.snowroad.file.service.FileService;
import com.snowroad.file.web.dto.EventsFileDetailResponseDTO;
import com.snowroad.file.web.dto.EventsFileUpdateRequestDTO;
import com.snowroad.admin.service.AdminService;
import com.snowroad.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "어드민 API", description = "어드민에서 사용하는 API")
public class AdminController {
    private final AdminService adminService;
    private final EventService eventService;
    private final FileService fileService;

    @Value("${access.token.cookie.expiry}")
    private int ACCESS_TOKEN_COOKIE_EXPIRY;

    @Value("${refresh.token.cookie.expiry}")
    private int REFRESH_TOKEN_COOKIE_EXPIRY;


    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);  // JavaScript에서 접근 불가능
        cookie.setPath("/");       // 모든 경로에서 사용 가능
        cookie.setMaxAge(maxAge);  // 쿠키 유효시간 설정
        response.addCookie(cookie);
    }

    @Operation(summary="어드민 로그인", description = "(관리자) 어드민 페이지에 로그인합니다.\n로그인 성공 시 쿠키에 Refresh token, Access token이 저장됩니다.")
    //@ApiResponse(responseCode = "200", description = "수신함 조회 성공", content = @Content(schema = @Schema(implementation = AlarmReceiveDto.class)))
    @PostMapping("/api/admin/login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequestDTO requestDto, HttpServletResponse response) {
        AdminLoginResponseDTO loginRes = adminService.login(requestDto.getId(), requestDto.getPassword());
        // CookieUtils를 사용하여 쿠키에 토큰 추가

        // 쿠키에 JWT 저장
        addCookie(response, "access_token", loginRes.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRY);
        addCookie(response, "refresh_token", loginRes.getRefreshToken(), REFRESH_TOKEN_COOKIE_EXPIRY);

        return new ResponseEntity<>("Login success", HttpStatus.OK);
    }

    @Operation(summary="팝업, 전시 리스트 조회", description = "(관리자) 등록된 팝업, 전시 리스트를 조회합니다.")
    @GetMapping("/api/admin/events")
    public ResponseEntity<PagedResponseDto<Events>> getList(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(eventService.getEventByPagination(page));
    }


    @Operation(summary="팝업, 전시 상세 조회", description = "(관리자) 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = EventsResponseDto.class)))
    @GetMapping("/api/admin/events/{id}")
    public EventsResponseDto findById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @Operation(summary="이벤트에 등록된 상세 이미지 조회", description = "(관리자) 이벤트에 등록된 상세 이미지를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = EventsFileDetailResponseDTO.class)))
    @GetMapping("/api/admin/events/{id}/files")
    public List<EventsFileDetailResponseDTO> getEventFilesDtlList(@PathVariable Long id) {
        return eventService.getEventFilesDtlList(id);
    }

    @Operation(summary="이벤트에 등록된 썸네일(메인) 이미지 조회", description = "(관리자) 이벤트에 등록된 상세 이미지를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = EventsFileDetailResponseDTO.class)))
    @GetMapping("/api/admin/events/{id}/main-files")
    public List<EventsFileDetailResponseDTO> getEventMainFilesDtlList(@PathVariable Long id) {
        return eventService.getTumbFilesDtlList(id);
    }

    @Operation(summary="팝업, 전시 등록", description = "(관리자) 이벤트를 등록합니다.")
    //@ApiResponse(responseCode = "200", description = "수신함 조회 성공", content = @Content(schema = @Schema(implementation = AlarmReceiveDto.class)))
    @PostMapping("/api/admin/events")
    public Long save(@RequestBody EventsSaveRequestDto requestDto) {
        return eventService.save(requestDto);
    }

    @Operation(
            summary = "(상세)이벤트 첨부파일 추가",
            description = "(관리자) 이벤트에 첨부된 파일을 추가합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = EventsFileUpdateRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "파일 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("/api/admin/events/{eventId}/file/detail")
    public ResponseEntity<String> addFileDetail(
            @PathVariable Long eventId,
            @RequestParam("file") MultipartFile file) {
        try {
            fileService.addFileDetail(eventId, file);
            return ResponseEntity.ok("파일 업로드 성공");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 실패: " + e.getMessage());
        }
    }
    @Operation(
            summary = "(메인)이벤트 첨부 파일 추가",
            description = "(관리자) 이벤트에 첨부된 파일을 추가합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = EventsFileUpdateRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "파일 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("/api/admin/events/{eventId}/file/main")
    public ResponseEntity<String> addFileMain(
            @PathVariable Long eventId,
            @RequestParam("file") MultipartFile file) {
        try {
            fileService.addFileMain(eventId, file);
            return ResponseEntity.ok("파일 업로드 성공");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 실패: " + e.getMessage());
        }
    }
    @Operation(summary="팝업, 전시 수정", description = "(관리자) 이벤트를 수정합니다.")
    @PutMapping("/api/admin/events/{id}")
    public Long update(@PathVariable Long id,
            @RequestBody EventsSaveRequestDto requestDto) {
        return eventService.update(id, requestDto);
    }


    @Operation(
            summary = "이벤트 첨부파일 수정",
            description = "(관리자) 이벤트에 첨부된 파일을 수정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = EventsFileUpdateRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "파일 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PutMapping("/api/admin/events/files/{fileId}")
    public ResponseEntity<String> updateFile(@PathVariable Long fileId
    , @RequestParam("file") MultipartFile file) {

        try {
            fileService.updateFileDetail(fileId, file);
            return ResponseEntity.ok("파일 수정 성공");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 수정 실패: " + e.getMessage());
        }

    }


    @Operation(summary="팝업, 전시 삭제", description = "(관리자) 이벤트를 삭제합니다.")
    @DeleteMapping("/api/admin/events/{eventId}")
    public Long delete(@PathVariable Long eventId) {
        eventService.delete(eventId);
        return eventId;
    }

    @Operation(summary="이벤트 첨부파일 삭제", description = "(관리자) 이벤트에 첨부된 파일을 삭제합니다.")
    @DeleteMapping("/api/admin/events/files/{fileId}")
    public Long deleteFile(@PathVariable Long fileId) {
        fileService.deleteFileDetail(fileId);
        return fileId;
    }

    @GetMapping("/api/admin/user")
    public ResponseEntity<?> getUserInfo(@CurrentUser UserDetails userDetails) {
        if (userDetails != null) {
            // 유저 정보가 있을 경우
            String username = userDetails.getUsername();
            return ResponseEntity.ok("Authenticated user: " + username);
        } else {
            // 유저 정보가 없을 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }
}
