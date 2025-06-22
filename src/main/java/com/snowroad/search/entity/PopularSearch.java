package com.snowroad.search.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="TB_PPLR_SRCH_M")
public class PopularSearch {

    @Schema(description = "인기검색ID")
    @Column(name = "PPLR_SRCH_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long pplrSrchId;

    @Schema(description = "인기검색내용")
    @Column(name = "PPLR_SRCH_CNTN")
    private String pplrSrchCntn;

    @Schema(description = "인기검색순위")
    @Column(name = "PPLR_SRCH_RANK")
    private String pplrSrchRank;

}
