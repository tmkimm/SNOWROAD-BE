package com.snowroad.search.map.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SearchRequestDTO {

    @Schema(description = "이벤트 텍스트 검색어")
    private String keyword;

    @Schema(description = "이벤트 타입에 대한 구분코드" )
    private String eventTypeCd;

    @Schema(description = "위도" , example = "37.550716")
    private Double latitude;  //위도

    @Schema(description = "경도" , example = "126.923044")
    private Double longitude; //경도

    @Schema(description = "카테고리 ID" , example = "IT")
    private String ctgyId;

    @Pattern(
            regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$",
            message = "날짜는 YYYYMMDD 형식이어야 하며, 유효한 날짜만 입력 가능합니다."
    )
    @Schema(description = "지도 조회조건 시작일자 (YYYYMMDD 형식)" , example = "20250228")
    private String operStatDt;

    @Pattern(
            regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$",
            message = "날짜는 YYYYMMDD 형식이어야 하며, 유효한 날짜만 입력 가능합니다."
    )
    @Schema(description = "지도 조회조건 종료일자 (YYYYMMDD 형식)" , example = "20250305")
    private String operEndDt;

    @Schema(description = "법정동코드")
    private String ldcd;

    @Schema(hidden = true)
    List<Long> eventIds;

    @Schema(hidden = true)
    private double minLat;  //바운딩 박스 - 최소 위도 값

    @Schema(hidden = true)
    private double maxLat;  //바운딩 박스 - 최대 위도 값

    @Schema(hidden = true)
    private double minLon; //바운딩 박스 - 최소 위도 값

    @Schema(hidden = true)
    private double maxLon; //바운딩 박스 - 최대 경도 값
}
