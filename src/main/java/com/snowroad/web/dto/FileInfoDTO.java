package com.snowroad.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "파일 정보(이미지) DTO")
public class FileInfoDTO {
    @Schema(description = "이미지 URL", example = "http://example.com/files/12345")
    private String imageUrl;

    @Schema(description = "모바일용 이미지 URL", example = "http://example.com/files/12345")
    private String smallImageUrl;
}
