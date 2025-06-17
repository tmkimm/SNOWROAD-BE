package com.snowroad.file.web.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@NoArgsConstructor
@Schema(description = "이벤트 파일 추가, 수정 DTO")
public class EventsFileUpdateRequestDTO {

    @Schema(
            description = "첨부 파일 목록 (최대 10개)",
            required = true,
            type = "array",
            format = "binary"
    )
    private List<MultipartFile> files;
}