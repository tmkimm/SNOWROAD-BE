package com.snowroad.event.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "팝업, 전시 상세 조회 DTO")
public class EventContentsResponseDto {
    @Schema(description = "이벤트ID")
    private Long eventId;
    @Schema(description = "이벤트명")
    private String eventNm;
    @Schema(description = "이벤트내용")
    private String eventCntn;
    @Schema(description = "이벤트주소")
    private String eventAddr;
    @Schema(description = "이벤트주소(도로명)")
    private String rads;
    @Schema(description = "이벤트주소(지번)")
    private String lnad;
    @Schema(description = "운영시작일자")
    private String operStatDt;
    @Schema(description = "운영종료일자")
    private String operEndDt;
    @Schema(description = "운영시간내용")
    private String operDttmCntn;
    @Schema(description = "카테고리 ID")
    private String ctgyId;
    @Schema(description = "팝업, 전시 구분 코드")
    private String eventTypeCd;
    @Schema(description = "좋아요 여부")
    private String likeYn;
    @Schema(description = "조회수")
    private int viewNwvl;

    @Schema(description = "메인 이미지 URL")
//    private String imageUrls;

    // 추가된 필드: 메인 이미지 URL 리스트
    private List<String> imageUrls; // 또는 List<String> mainImageUrls;

    @Schema(description = "모바일 이미지 URL")
    private String smallImageUrl;

    @Schema(description = "이벤트 상세 URL")
    private String eventDetailUrl;

    // 250601 QueryDSL Projections.constructor를 위한 생성자 추가
    // @AllArgsConstructor가 있지만, QueryDSL Projections.constructor는 List<String>을 직접 매핑하기 어려우므로 imageUrls을 위한 추가작업
    // => 쿼리에서 직접 매핑할 수 있는 필드들만 포함하는 생성자를 명시적으로 정의함.

    // 쿼리에서 받아올 필드만 포함하는 생성자
    public EventContentsResponseDto(
            Long eventId, String eventNm, String eventCntn, String eventAddr, String rads, String lnad,
            String operStatDt, String operEndDt, String operDttmCntn,
            String ctgyId, String eventTypeCd, String likeYn, int viewNwvl,
            String smallImageUrl, String eventDetailUrl // imageUrl은 List로 따로 세팅
    ) {
        this.eventId = eventId;
        this.eventNm = eventNm;
        this.eventCntn = eventCntn;
        this.eventAddr = eventAddr;
        this.rads = rads;
        this.lnad = lnad;
        this.operStatDt = operStatDt;
        this.operEndDt = operEndDt;
        this.operDttmCntn = operDttmCntn;
        this.ctgyId = ctgyId;
        this.eventTypeCd = eventTypeCd;
        this.likeYn = likeYn;
        this.viewNwvl = viewNwvl;
        this.smallImageUrl = smallImageUrl;
        this.eventDetailUrl = eventDetailUrl;
    }

}
