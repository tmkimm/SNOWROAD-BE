package com.snowroad.mark.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "즐겨찾기 팝업/전시 리스트페이지 조회 DTO")
public class MarkedEventResponseDTO {
    @Schema(description = "이벤트ID")
    private Long eventId;
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
    @Schema(description = "카테고리 ID")
    private String ctgyId;
    /*    @Schema(description = "카테고리 명")
        private String ctgyNm;*/
    @Schema(description = "팝업, 전시 구분 코드")
    private String eventTypeCd;
    @Schema(description = "좋아요 여부")
    private String likeYn;

    @Schema(description = "메인 이미지 URL")
    private String imageUrl;

    @Schema(description = "모바일 이미지 URL")
    private String smallImageUrl;
}
