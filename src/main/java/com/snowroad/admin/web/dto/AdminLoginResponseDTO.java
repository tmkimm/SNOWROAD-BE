package com.snowroad.admin.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class AdminLoginResponseDTO {
    private String accessToken;

    private String refreshToken;
}
