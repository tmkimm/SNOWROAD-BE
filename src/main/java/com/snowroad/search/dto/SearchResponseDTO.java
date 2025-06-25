package com.snowroad.search.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponseDTO {

    @Id
    private Long eventId;

    @Schema(description = "이벤트명")
    private String eventNm;

    @Schema(description = "이벤트내용")
    private String eventCntn;

    @Schema(description = "이벤트주소")
    private String eventAddr;

    @Schema(description = "도로명주소")
    private String rads;

    @Schema(description = "지번주소")
    private String lnad;

    @Schema(description = "운영시작일자")
    private String operStatDt;

    @Schema(description = "운영종료일자")
    private String operEndDt;

    @Schema(description = "운영시간내용")
    private String operDttmCntn;

    @Schema(description = "카테고리 ID")
    private String ctgyId;

    @Schema(description = "이벤트 구분 코드")
    private String eventTypeCd;

    @Schema(description = "위도")
    private Double addrLttd;

    @Schema(description = "경도")
    private Double addrLotd;

    @Schema(description = "법정동코드")
    private String ldcd;

    @Schema(description = "이미지 URL")
    private String fileThumbUrl;

    @Schema(description = "파일")
    private Long evntFileId;

    @Schema(description = "조회수")
    private int viewNmvl;

    @Schema(description = "거리")
    private Double distanceKm;

    @Schema(description = "사용자표시거리")
    private String displayDistance;

    @QueryProjection
    public SearchResponseDTO(Long eventId, String eventNm, String eventCntn, String eventAddr, String rads,
                             String lnad, String operStatDt, String operEndDt, String operDttmCntn,
                             String ctgyId, String eventTypeCd, Double addrLttd, Double addrLotd,
                             String ldcd, String fileThumbUrl, Long evntFileId, int viewNmvl) {
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
        this.addrLttd = addrLttd;
        this.addrLotd = addrLotd;
        this.fileThumbUrl = fileThumbUrl;
        this.ldcd = ldcd;
        this.evntFileId = evntFileId;
        this.viewNmvl = viewNmvl;
    }
}
