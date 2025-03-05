package com.snowroad.auth.web;

import com.snowroad.auth.web.dto.UserInfoResponseDto;
import com.snowroad.common.exception.UnauthorizedException;
import com.snowroad.common.util.CurrentUser;
import com.snowroad.config.auth.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API", description = "소셜 로그인 및 회원 정보 조회 관련 API")
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Operation(
            summary = "Get User Authentication Status",
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
}