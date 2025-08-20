package com.snowroad.event.web.dto;

import com.snowroad.entity.Events;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "팝업, 전시 등록 DTO")
public class EventsSaveRequestDto {
    @Schema(description = "이벤트명", example = "올리브영 3월 팝업스토어")
    private String eventNm;
    @Schema(description = "이벤트내용", example = "올리브영 3월 팝업리스트 소개합니다")
    private String eventCntn;
    @Schema(description = "이벤트주소")
    private String eventAddr;
    @Schema(description = "운영시작일자", example = "20250301")
    private String operStatDt;
    @Schema(description = "운영종료일자", example = "20250331")
    private String operEndDt;
    @Schema(description = "운영시간내용", example = "월~일 : 09:00 - 18:00")
    private String operDttmCntn;
    @Schema(description = "카테고리 ID", example = "ENTER")
    private String ctgyId;
    @Schema(description = "팝업, 전시 구분 코드", example = "PPST")
    private String ppstEnbnTypeCd;
    @Schema(description = "위도", example = "126.9780")
    private double addrLttd;
    @Schema(description = "경도", example = "37.5665")
    private double addrLotd;

    @Schema(description = "도로명주소", example = "서울 마포구 홍익로 10")
    private String rads;

    @Schema(description = "지번주소", example = "서울 마포구 서교동 486")
    private String lnad;

    @Schema(description = "법정동코드", example = "111000050")
    private String ldcd;

    @Schema(description = "이벤트 상세 URL", example = "https://groundseesaw.co.kr/product/detail.html?product_no=1265&cate_no=47&https://groundseesaw.co.kr/product/detail.html?product_no=1265")
    private String eventDetailUrl;

    @Builder
    public EventsSaveRequestDto(String eventNm, String eventCntn, String eventAddr, String operStatDt, String operEndDt, String operDttmCntn, String ctgyId, String ppstEnbnTypeCd, double addrLttd, double addrLotd, String ldcd, String eventDetailUrl) {
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
        this.eventDetailUrl = eventDetailUrl;
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
                .rads(rads)
                .lnad(lnad)
                .eventDetailUrl(eventDetailUrl)
                .ldcd(ldcd)
                .build();
    }
}
