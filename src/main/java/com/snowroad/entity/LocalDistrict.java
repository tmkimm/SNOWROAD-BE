package com.snowroad.entity;

import com.snowroad.common.domain.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_LDCD_C")
@Schema(description = "법정동코드") // 엔티티 전체 설명
public class LocalDistrict extends BaseTimeEntity {

    @Schema(description = "법정동코드")
    @Id // PK
    @Column(name = "LDCD", length = 20, nullable = false)
    private String ldcd;

    @Schema(description = "법정동코드명")
    @Column(name = "LDCD_NM", length = 100, nullable = false)
    private String ldcdNm;

    @Schema(description = "상위법정동코드")
    @Column(name = "UPPR_LDCD", length = 20) // nullable 여부 확인 필요, ERD에 명시 안됨
    private String upprLdcd;

    @Schema(description = "지역그룹단위")
    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계, LAZY 로딩 사용
    @JoinColumn(name = "RGNT_CD", referencedColumnName = "RGNT_CD", nullable = false) // 외래키 컬럼명과 참조하는 Region의 PK 컬럼명 명시
    private Region region;

    @Schema(description = "지역그룹단위상세코드")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RGNT_DELT_CD", referencedColumnName = "RGNT_DELT_CD", nullable = false)
    private TbRgntDeltC tbRgntDeltC;

}