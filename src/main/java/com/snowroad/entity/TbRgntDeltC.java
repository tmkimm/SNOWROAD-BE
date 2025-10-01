package com.snowroad.entity;

import com.snowroad.common.domain.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_RGNT_DELT_C")
@Schema(description = "ENT_지역그룹단위상세코드") // 엔티티 전체 설명
public class TbRgntDeltC extends BaseTimeEntity {

    @Schema(description = "지역그룹단위상세코드")
    @Id // PK
    @Column(name = "RGNT_DELT_CD", length = 10, nullable = false)
    private String rgntDeltCd;

    @Schema(description = "지역그룹단위상세명")
    @Column(name = "RGNT_DELT_NM", length = 100, nullable = false)
    private String rgntDeltNm;

}