package com.snowroad.auth.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "사용자 정보 반환 DTO")
@Getter
@RequiredArgsConstructor
public class UserInfoResponseDto {
    @Schema(description = "사용자 ID", example = "1", required = true)
    private final Long userId;

    @Schema(description = "사용자 이름", example = "testuser", required = true)
    private final String username;

    @Schema(description = "회원가입 여부 (Y/N)", example = "Y", required = true)
    private final String joinYn;;
}