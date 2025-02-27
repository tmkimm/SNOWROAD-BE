package com.snowroad.event.web.dto;

import com.snowroad.entity.Events;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "팝업, 전시 등록 DTO")
public class EventsSaveRequestDto {
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
    @Schema(description = "위도", example = "126.9780")
    private double addrLttd;
    @Schema(description = "경도", example = "37.5665")
    private double addrLotd;

    @Schema(description = "법정동코드", example = "111000050")
    private String ldcd;

    @Builder
    public EventsSaveRequestDto(String eventNm, String eventCntn, String eventAddr, String operStatDt, String operEndDt, String operDttmCntn, String ctgyId, String ppstEnbnTypeCd, double addrLttd, double addrLotd, String ldcd) {
        this.eventNm = eventNm;
        this.eventCntn = eventCntn;
        this.eventAddr = eventAddr;
        this.operStatDt = operStatDt;
        this.operEndDt = operEndDt;
        this.operDttmCntn = operDttmCntn;
        this.ctgyId = ctgyId;
        this.ppstEnbnTypeCd = ppstEnbnTypeCd;
        this.addrLttd = addrLttd;
        this.addrLotd = addrLotd;
        this.ldcd = ldcd;
    }


    public Events toEntity() {
        return Events.builder()
                .eventNm(eventNm)
                .eventCntn(eventCntn)
                .eventAddr(eventAddr)
                .operStatDt(operStatDt)
                .operEndDt(operEndDt)
                .operDttmCntn(operDttmCntn)
                .ctgyId(ctgyId)
                .eventTypeCd(ppstEnbnTypeCd)
                .addrLttd(addrLttd)
                .addrLotd(addrLotd)
                .build();
    }
}
