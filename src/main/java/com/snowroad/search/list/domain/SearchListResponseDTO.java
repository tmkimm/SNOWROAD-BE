package com.snowroad.search.list.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * 검색 인터페이스
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-14
 *
 */
@Entity
@Table(name = "TB_EVNT_M")
@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
@Schema(description = "이벤트 검색")
public class SearchListResponseDTO {

    @Id
    @Schema(description = "이벤트ID")
    @Column(name = "EVNT_ID")
    private Long evntId;

    @Schema(description = "이벤트명")
    @Column(name = "EVNT_NM")
    private String evntNm;

    @Schema(description = "이벤트내용")
    @Column(name = "EVNT_CNTN")
    private String evntCntn;

    @Schema(description = "이벤트주소")
    @Column(name = "EVNT_ADDR")
    private String evntAddr;

    @Schema(description = "운영시작일자")
    @Column(name = "OPER_STAT_DT")
    private String operStatDt;

    @Schema(description = "운영종료일자")
    @Column(name = "OPER_END_DT")
    private String operEndDt;

    @Schema(description = "운영시간내용")
    @Column(name = "OPER_DTTM_CNTN")
    private String operDttmCntn;

    @Schema(description = "이벤트구분코드")
    @Column(name = "EVNT_TYPE_CD")
    private String evntTypeCd;

    @Schema(description = "주소위도")
    @Column(name = "ADDR_LTTD")
    private Double addrLttd;

    @Schema(description = "주소경도")
    @Column(name = "ADDR_LOTD")
    private Double addrLotd;

    @Schema(description = "법정동코드")
    @Column(name = "LDCD")
    private String ldcd;

    @Schema(description = "카테고리ID")
    @Column(name = "CTGY_ID")
    private String ctgyId;

}
