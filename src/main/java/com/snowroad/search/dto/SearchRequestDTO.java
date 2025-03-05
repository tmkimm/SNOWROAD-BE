package com.snowroad.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 *
 * 검색 DTO
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-03-05
 *
 */
@Getter
@Setter
@ToString
@Schema(description = "검색 요청 DTO")
public class SearchRequestDTO {

    @Schema(description = "이벤트 텍스트 검색어")
    private String keyword;

    @Schema(description = "이벤트 타입에 대한 구분코드", example = "PPST")
    private String eventTypeCd;

    @Schema(description = "위도" , example = "37.527097226615")
    private Double latitude;  //위도

    @Schema(description = "경도" , example = "126.92730122817")
    private Double longitude; //경도

    @Schema(description = "카테고리 ID" , example = "CHAR")
    private String ctgyId;

    @Pattern(
            regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$",
            message = "날짜는 YYYYMMDD 형식이어야 하며, 유효한 날짜만 입력 가능합니다."
    )
    @Schema(description = "지도 조회조건 시작일자 (YYYYMMDD 형식)" , example = "20250101")
    private String operStatDt;

    @Pattern(
            regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$",
            message = "날짜는 YYYYMMDD 형식이어야 하며, 유효한 날짜만 입력 가능합니다."
    )
    @Schema(description = "지도 조회조건 종료일자 (YYYYMMDD 형식)" , example = "20250331")
    private String operEndDt;

    @Schema(hidden = true , description = "법정동코드")
    private String ldcd;

    @Schema(hidden = true)
    private List<Long> eventIds;

    @Schema(hidden = true)
    private double minLat;  //바운딩 박스 - 최소 위도 값

    @Schema(hidden = true)
    private double maxLat;  //바운딩 박스 - 최대 위도 값

    @Schema(hidden = true)
    private double minLon; //바운딩 박스 - 최소 위도 값

    @Schema(hidden = true)
    private double maxLon; //바운딩 박스 - 최대 경도 값
}
