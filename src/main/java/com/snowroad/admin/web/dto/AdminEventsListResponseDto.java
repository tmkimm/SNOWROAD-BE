package com.snowroad.admin.web.dto;

import com.snowroad.event.domain.Events;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "(어드민)팝업, 전시 상세 조회 DTO")
public class AdminEventsListResponseDto {
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

    @Schema(description = "팝업, 전시 구분 코드")
    private String ppstEnbnTypeCd;

    @Schema(description = "위도")
    private Double addrLttd;    // TODO 추후 POINT 타입으로 변경

    @Schema(description = "경도")
    private Double addrLotd;

    @Schema(description = "삭제유무")
    private String deleteYn = "N";

    @Schema(description = "법정동코드")
    private String ldcd;

    public AdminEventsListResponseDto(Events entity) {
        this.eventId = entity.getEventId();
        this.eventNm = entity.getEventNm();
        this.eventCntn = entity.getEventCntn();
        this.eventAddr = entity.getEventAddr();
        this.operStatDt = entity.getOperStatDt();
        this.operEndDt = entity.getOperEndDt();
        this.operDttmCntn = entity.getOperDttmCntn();
        this.ctgyId = entity.getCtgyId();
        this.ppstEnbnTypeCd = entity.getPpstEnbnTypeCd();
        this.addrLttd = entity.getAddrLttd();
        this.addrLotd = entity.getAddrLotd();
        this.deleteYn = entity.getDeleteYn();
        this.ldcd = entity.getLdcd();
    }
}
