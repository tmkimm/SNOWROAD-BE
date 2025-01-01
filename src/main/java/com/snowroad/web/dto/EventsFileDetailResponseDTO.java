package com.snowroad.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class EventsFileDetailResponseDTO {

    @Schema(description = "파일 디테일 ID")
    private Long fileDtlId;

    @Schema(description = "파일 URL")
    private String fileUrl;

    @Schema(description = "원본 파일명")
    private String origFileNm;


}
