package com.snowroad.event.web.dto;

import com.snowroad.event.domain.Events;
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


    @Schema(description = "위도")
    private Double addrLttd;

    @Schema(description = "경도")
    private Double addrLotd;


    public EventsResponseDto(Long eventId, String eventNm, String eventCntn, String eventAddr, String operStatDt, String operEndDt, String operDttmCntn, String ctgyId, String ppstEnbnTypeCd, String ctgyNm, Double addrLttd, Double addrLotd) {
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
        this.ppstEnbnTypeCd = entity.getPpstEnbnTypeCd();
        this.addrLttd = entity.getAddrLttd();
        this.addrLotd = entity.getAddrLotd();
    }

}
