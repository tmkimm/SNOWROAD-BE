package com.snowroad.event.web.dto;

import com.snowroad.entity.Events;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "팝업, 전시 상세 조회 DTO")
public class EventsResponseDto {
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

    @Schema(description = "카테고리 명")
    private String ctgyNm;

    @Schema(description = "도로명주소", example = "서울 마포구 홍익로 10")
    private String rads;

    @Schema(description = "지번주소", example = "서울 마포구 서교동 486")
    private String lnad;

    @Schema(description = "위도")
    private Double addrLttd;

    @Schema(description = "경도")
    private Double addrLotd;

    @Schema(description = "이벤트 상세 URL", example = "https://groundseesaw.co.kr/product/detail.html?product_no=1265&cate_no=47&https://groundseesaw.co.kr/product/detail.html?product_no=1265")
    private String eventDetailUrl;
    public EventsResponseDto(Long eventId, String eventNm, String eventCntn, String eventAddr, String operStatDt, String operEndDt, String operDttmCntn, String ctgyId, String ppstEnbnTypeCd, String ctgyNm, Double addrLttd, Double addrLotd, String eventDetailUrl) {
        this.eventId = eventId;
        this.eventNm = eventNm;
        this.eventCntn = eventCntn;
        this.eventAddr = eventAddr;
        this.operStatDt = operStatDt;
        this.operEndDt = operEndDt;
        this.operDttmCntn = operDttmCntn;
        this.ctgyId = ctgyId;
        this.ppstEnbnTypeCd = ppstEnbnTypeCd;
        this.ctgyNm = ctgyNm;
        this.addrLttd = addrLttd;
        this.addrLotd = addrLotd;
        this.eventDetailUrl = eventDetailUrl;
    }
//    @Schema(description = "메인 이미지 정보")
//    private FileInfoDTO mainImage;
//
//    @Schema(description = "내용 이미지 정보(배열)")
//    private List<FileInfoDTO> contentImages;
//
    public EventsResponseDto(Events entity) {
        this.eventId = entity.getEventId();
        this.eventNm = entity.getEventNm();
        this.eventCntn = entity.getEventCntn();
        this.eventAddr = entity.getEventAddr();
        this.operStatDt = entity.getOperStatDt();
        this.operEndDt = entity.getOperEndDt();
        this.operDttmCntn = entity.getOperDttmCntn();
        this.ctgyId = entity.getCtgyId();
        this.ppstEnbnTypeCd = entity.getEventTypeCd();
        this.addrLttd = entity.getAddrLttd();
        this.addrLotd = entity.getAddrLotd();
        this.rads = entity.getRads();
        this.lnad = entity.getLnad();
        this.eventDetailUrl = entity.getEventDetailUrl();
    }

}
