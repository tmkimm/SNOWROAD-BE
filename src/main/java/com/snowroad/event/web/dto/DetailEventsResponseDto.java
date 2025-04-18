package com.snowroad.event.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "팝업, 전시 리스트페이지 조회 DTO")
public class DetailEventsResponseDto {
    @Schema(description = "이벤트ID")
    private Long eventId;
    @Schema(description = "이벤트명")
    private String eventNm;
    @Schema(description = "이벤트내용")
    private String eventCntn;
    @Schema(description = "이벤트주소")
    private String eventAddr;
    @Schema(description = "이벤트 시/동 주소")
    private String lnad;
    @Schema(description = "운영시작일자")
    private String operStatDt;
    @Schema(description = "운영종료일자")
    private String operEndDt;
    @Schema(description = "카테고리 ID")
    private String ctgyId;
    @Schema(description = "팝업, 전시 구분 코드")
    private String eventTypeCd;
    @Schema(description = "좋아요 여부")
    private String likeYn;
    @Schema(description = "메인 이미지 URL")
    private String imageUrl;

    @Schema(description = "모바일 이미지 URL")
    private String smallImageUrl;
    @Schema(description = "조회수")
    private int viewNmvl;

    @QueryProjection
    public DetailEventsResponseDto(Long eventId, String eventNm, String eventCntn, String eventAddr, String lnad, String operStatDt, String operEndDt, String ctgyId, String eventTypeCd, String likeYn, String imageUrl, String smallImageUrl, Integer viewNmvl) {
        this.eventId = eventId;
        this.eventNm = eventNm;
        this.eventCntn = eventCntn;
        this.eventAddr = eventAddr;
        this.lnad = lnad;
        this.operStatDt = operStatDt;
        this.operEndDt = operEndDt;
        this.ctgyId = ctgyId;
        this.eventTypeCd = eventTypeCd;
        this.likeYn = likeYn;
        this.imageUrl = imageUrl;
        this.smallImageUrl = smallImageUrl;
        this.viewNmvl = viewNmvl;
    }

}
