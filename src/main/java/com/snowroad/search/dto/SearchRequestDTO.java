package com.snowroad.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @Schema(description = "검색 키워드" , nullable = true)
    private String keyword;

    @Schema(description = "정렬 유형 (값이 없거나 지정 값이 아닌 경우 오픈일자로 정렬됩니다)" , nullable = true, example = "10")
    private String sortType;

    @NotBlank
    @Min(0)
    @Schema(description = "페이지", example = "0", defaultValue = "0")
    private Integer page;

    @NotBlank
    @Min(0)
    @Schema(description = "페이지 사이즈", example = "12", defaultValue = "12")
    private Integer pageSize;

    @Schema(description = "위도" , nullable = true , example = "37.527097226615")
    private Double latitude;  //위도

    @Schema(description = "경도" , nullable = true , example = "126.92730122817")
    private Double longitude; //경도

    @Pattern(
        regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$",
        message = "날짜는 YYYYMMDD 형식이어야 하며, 유효한 날짜만 입력 가능합니다."
    )
    @Schema(description = "시작일자 (YYYYMMDD 형식)", nullable = true , example = "20250101")
    private String operStatDt;

    @Pattern(
        regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$",
        message = "날짜는 YYYYMMDD 형식이어야 하며, 유효한 날짜만 입력 가능합니다."
    )
    @Schema(description = "종료일자 (YYYYMMDD 형식)", nullable = true , example = "20250331")
    private String operEndDt;

    @NotBlank
    @Schema(description = "이벤트구분코드", example = "10")
    private String eventTypeCd;

    @Schema(description = "카테고리 [ex: categories=BEAU&categories=CHAR]" , nullable = true)
    private List<String> categories;

    @Schema(description = "지역그룹단위필터 [ex: regionGroups=10&regionGroups=20]" , nullable = true )
    private List<String> regionGroups;

    @Schema(hidden = true)
    private List<Long> eventIds;

    @Schema(hidden = true)
    private Long userAcntNo;

    public boolean hasKeyword() {
        return keyword != null;
    }

    public boolean hasSortType() {
        return sortType != null;
    }

    public boolean hasCoordinate() {
        return latitude != null && longitude != null;
    }

    public boolean hasEventTypeCd() {
        return eventTypeCd != null;
    }

    public boolean hasDateAllBoolean() {
        return operStatDt != null && operEndDt != null;
    }

    public boolean hasCategories() {
        return categories != null && !categories.isEmpty();
    }

    public boolean hasRegionGroups() {
        return regionGroups != null && !regionGroups.isEmpty();
    }
}
