package com.snowroad.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "파일 정보(이미지) DTO")
public class AdminLoginRequestDTO {
    @Schema(description = "아이디")
    private String id;

    @Schema(description = "비밀번호")
    private String password;
}
