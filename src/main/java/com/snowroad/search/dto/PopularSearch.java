package com.snowroad.search.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PopularSearch {

    @Schema(description = "인기검색ID")
    private Long pplrSrchId;

    @Schema(description = "인기검색내용")
    private String pplrSrchCntn;

    @Schema(description = "인기검색순위")
    private String pplrSrchRank;

    @QueryProjection
    public PopularSearch(Long pplrSrchId, String pplrSrchCntn, String pplrSrchRank) {
        this.pplrSrchId = pplrSrchId;
        this.pplrSrchCntn = pplrSrchCntn;
        this.pplrSrchRank = pplrSrchRank;
    }
}
