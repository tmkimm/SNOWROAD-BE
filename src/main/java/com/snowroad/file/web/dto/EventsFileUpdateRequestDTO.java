package com.snowroad.file.web.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
@NoArgsConstructor
@Schema(description = "이벤트 파일 수정 DTO")
public class EventsFileUpdateRequestDTO {
    @Schema(description = "대표 이미지 ", required = true)
    private MultipartFile file;
}