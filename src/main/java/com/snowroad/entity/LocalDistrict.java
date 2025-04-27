package com.snowroad.entity;

import com.snowroad.common.domain.BaseTimeEntity; // BaseTimeEntity import
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder; // Builder 추가
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
    // @GeneratedValue - 이 PK는 IDENTITY가 아닌 코드 값인 것으로 보이므로, 자동 생성을 사용하지 않습니다.
    @Column(name = "LDCD", length = 20, nullable = false)
    private String ldcd;

    @Schema(description = "법정동코드명")
    @Column(name = "LDCD_NM", length = 100, nullable = false)
    private String ldcdNm;

    @Schema(description = "상위법정동코드")
    @Column(name = "UPPR_LDCD", length = 20) // nullable 여부 확인 필요, ERD에 명시 안됨
    private String upprLdcd;

    // BaseTimeEntity가 DATA_CRTN_DTTM, DATA_EDIT_DTTM을 처리합니다.

    // TB_RGNT_C (Region)와의 N:1 관계 설정 (LocalDistrict N : 1 Region)
    // TB_LDCD_C 테이블의 RGNT_CD 컬럼이 TB_RGNT_C 테이블의 RGNT_CD를 참조합니다.
    @Schema(description = "지역그룹단위")
    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계, LAZY 로딩 사용
    @JoinColumn(name = "RGNT_CD", referencedColumnName = "RGNT_CD", nullable = false) // 외래키 컬럼명과 참조하는 Region의 PK 컬럼명 명시
    private Region region;

    @Builder // 객체 생성 시 Builder 패턴 사용 가능
    public LocalDistrict(String ldcd, String ldcdNm, String upprLdcd, Region region) {
        this.ldcd = ldcd;
        this.ldcdNm = ldcdNm;
        this.upprLdcd = upprLdcd;
        this.region = region;
    }

    // 엔티티 업데이트 메소드 (필요하다면 추가, 예: 상위 코드 또는 이름 변경)
    // @Schema(description = "법정동 이름 변경")
    // public void updateLocalDistrictName(String ldcdNm) {
    //    this.ldcdNm = ldcdNm;
    // }

    // @Schema(description = "상위 법정동 변경")
    // public void updateUpperLocalDistrict(String upprLdcd) {
    //     this.upprLdcd = upprLdcd;
    // }

    // @Schema(description = "지역 그룹 변경")
    // public void updateRegion(Region region) {
    //     this.region = region;
    // }
}