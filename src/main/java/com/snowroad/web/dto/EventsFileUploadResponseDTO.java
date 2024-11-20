package com.snowroad.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class EventsFileUploadResponseDTO {

    @Schema(description = "파일 업로드 성공 메시지")
    private String message;

    @Schema(description = "업로드된 파일들의 정보", required = true)
    private List<FileInfoDTO> files;


}
