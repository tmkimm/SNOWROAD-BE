package com.snowroad.auth.web;

import com.snowroad.auth.web.dto.SignUpRequestDto;
import com.snowroad.auth.web.dto.UserInfoResponseDto;
import com.snowroad.common.exception.ForbiddenException;
import com.snowroad.common.exception.UnauthorizedException;
import com.snowroad.common.util.CookieUtil;
import com.snowroad.common.util.CurrentUser;
import com.snowroad.config.auth.dto.CustomUserDetails;
import com.snowroad.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API", description = "소셜 로그인 및 회원 정보 조회 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CookieUtil cookieUtil;
    private final UserService userService;

    @Operation(
            summary = "사용자 정보 조회",
            description = "소셜 로그인 후 사용자 정보를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping("/user-info")
    public UserInfoResponseDto getAuthStatus(@CurrentUser CustomUserDetails userDetails) {
        if(userDetails == null) {
            throw new UnauthorizedException("로그인되지 않았습니다.");
        }
        UserInfoResponseDto userInfo = new UserInfoResponseDto(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getJoinYn()
        );
        return userInfo;
    }
    @Operation(
            summary = "로그아웃",
            description = "자동 로그인을 해제하며 로그아웃합니다."
    )
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // 인증 관련 쿠키 클리어
        cookieUtil.clearCookies(response);
        return ResponseEntity.noContent().build();
    }
}