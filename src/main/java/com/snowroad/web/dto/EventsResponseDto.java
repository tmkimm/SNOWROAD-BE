package com.snowroad.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "팝업, 전시 상세 조회 DTO")
public class EventsResponseDto {
    @Schema(description = "이벤트ID")
    private String eventId;
    @Schema(description = "이벤트명")
    private String eventNm;
    @Schema(description = "이벤트내용")
    private String eventCntn;
    @Schema(description = "이벤트주소")
    private String eventAddr;
    @Schema(description = "운영시작일자")
    private String operStatDt;
    @Schema(description = "운영종료일자")
    private String operEndDt;
    @Schema(description = "운영시간내용")
    private String operDttmCntn;
    @Schema(description = "카테고리 ID")
    private String ctgyId;
    @Schema(description = "카테고리 명")
    private String ctgyNm;
    @Schema(description = "팝업, 전시 구분 코드")
    private String ppstEnbnTypeCd;

    @Schema(description = "메인 이미지 정보")
    private FileInfoDTO mainImage;

    @Schema(description = "내용 이미지 정보(배열)")
    private List<FileInfoDTO> contentImages;

}
