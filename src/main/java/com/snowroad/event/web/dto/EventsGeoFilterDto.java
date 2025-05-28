package com.snowroad.event.web.dto;

import com.snowroad.entity.Events;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "팝업, 전시 상세 조회 DTO")
public class EventsGeoFilterDto {

    // SQL: TRC.RGNT_CD -> DTO: 지역그룹단위코드 (rgntCd)
    @Schema(description = "지역그룹단위코드")
    private String rgntCd;

    // SQL: TRC.RGNT_NM -> DTO: 지역그룹단위명 (DTO 양식에 없었지만, SQL 결과에 있으므로 추가)
    @Schema(description = "지역그룹단위명")
    private String regionName; // DTO 양식에 없던 필드, SQL 결과에 맞춰 추가

    // SQL: TRC.RGNT_CD -> DTO: 지역그룹단위구분코드 (rgntCd)
    @Schema(description = "지역그룹단위구분코드")
    private String rgntTypeCd;

    // SQL: COUNT(TLC.LDCD_NM) AS CNT -> DTO: 연결법정동코드수 (cnt)
    @Schema(description = "연결법정동코드수")
    private Long cnt; // DTO 양식과 동일

    // SQL: GROUP_CONCAT(TLC.LDCD_NM) AS LDCD_NM -> DTO: 연결법정동코드명 (lcdcNm). DTO 양식과 SQL alias명이 약간 다르지만, 의미상 매칭됩니다.
    @Schema(description = "연결법정동코드명")
    private String lcdcNm; // DTO 양식과 동일


}
