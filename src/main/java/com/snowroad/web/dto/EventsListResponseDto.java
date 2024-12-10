package com.snowroad.web.dto;

import com.snowroad.domain.events.Events;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "팝업, 전시 상세 조회 DTO")
public class EventsListResponseDto {
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
    @Schema(description = "운영시간내용")
    private String operDttmCntn;
    @Schema(description = "카테고리 ID")
    private String ctgyId;
    @Schema(description = "카테고리 명")
    private String ctgyNm;
    @Schema(description = "팝업, 전시 구분 코드")
    private String ppstEnbnTypeCd;
    @Schema(description = "팝업, 전시 구분 명")
    private String ppstEnbnTypeNm;

    @Schema(description = "메인 이미지 URL")
    private String imageUrl;

    @Schema(description = "모바일 이미지 URL")
    private String smallImageUrl;


    public EventsListResponseDto(Long eventId, String eventNm, String eventCntn, String eventAddr, String operStatDt, String operEndDt, String operDttmCntn, String ctgyId, String ctgyNm, String ppstEnbnTypeCd, String ppstEnbnTypeNm, String imageUrl, String smallImageUrl) {
        this.eventId = eventId;
        this.eventNm = eventNm;
        this.eventCntn = eventCntn;
        this.eventAddr = eventAddr;
        this.operStatDt = operStatDt;
        this.operEndDt = operEndDt;
        this.operDttmCntn = operDttmCntn;
        this.ctgyId = ctgyId;
        this.ctgyNm = ctgyNm;
        this.ppstEnbnTypeCd = ppstEnbnTypeCd;
        this.ppstEnbnTypeNm = ppstEnbnTypeNm;
        this.imageUrl = imageUrl;
        this.smallImageUrl = smallImageUrl;
    }
}
