package com.snowroad.search.map.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
@Schema(description = "이벤트 데이터 Map 커스텀 마커 조회")
public class EventsGeoMapDto {

    @Id
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

    @Schema(description = "이벤트구분코드")
    private String evntTypeCd;

    @Schema(description = "주소위도")
    private double addrLttd;

    @Schema(description = "주소경도")
    private double addrLotd;

    @Schema(description = "법정동코드")
    private String ldcd;

    @Schema(description = "카테고리ID")
    private String ctgyId;

    @Schema(description = "카테고리명")
    private String ctgyNm;

    @Schema(description = "이벤트구분코드")
    private String ppstEnbnTypeCd;

    @Schema(description = "이벤트구분명")
    private String ppstEnbnTypeNm;
}
