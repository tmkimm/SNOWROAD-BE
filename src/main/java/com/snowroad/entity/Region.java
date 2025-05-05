package com.snowroad.entity;

import com.snowroad.common.domain.BaseTimeEntity; // BaseTimeEntity import
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder; // Builder 추가
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList; // Optional: for OneToMany
import java.util.List; // Optional: for OneToMany

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_RGNT_C")
@Schema(description = "지역그룹단위코드") // 엔티티 전체 설명
public class Region extends BaseTimeEntity {

    @Schema(description = "지역그룹단위코드")
    @Id // PK
    // @GeneratedValue - 이 PK는 IDENTITY가 아닌 코드 값인 것으로 보이므로, 자동 생성을 사용하지 않습니다.
    @Column(name = "RGNT_CD", length = 10, nullable = false)
    private String rgntCd;

    @Schema(description = "지역그룹단위명")
    @Column(name = "RGNT_NM", length = 100, nullable = false)
    private String rgntNm;

    @Schema(description = "지역그룹구분코드")
    @Column(name = "RGNT_TYPE_CD", length = 2, nullable = false)
    private String rgntTypeCd;

    // BaseTimeEntity가 DATA_CRTN_DTTM, DATA_EDIT_DTTM을 처리합니다.

    // TB_LDCD_C (LocalDistrict)와의 1:N 관계 설정 (Region 1 : N LocalDistrict)
    // Region을 통해 속한 LocalDistrict 목록을 조회할 필요가 있다면 추가합니다.
    // mappedBy 값은 LocalDistrict 엔티티에서 Region을 참조하는 필드 이름과 일치해야 합니다.
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY) // LAZY 로딩 사용
    private List<LocalDistrict> localDistricts = new ArrayList<>(); // null 방지를 위해 초기화

    @Builder // 객체 생성 시 Builder 패턴 사용 가능
    public Region(String rgntCd, String rgntNm, String rgntTypeCd) {
        this.rgntCd = rgntCd;
        this.rgntNm = rgntNm;
        this.rgntTypeCd = rgntTypeCd;
    }

    // 엔티티 업데이트 메소드 (필요하다면 추가, 예: 이름 변경)
    // @Schema(description = "지역 이름 변경") // 메소드 자체에 대한 Schema 설명은 흔치 않지만 예시로
    // public void updateRegionName(String rgntNm) {
    //     this.rgntNm = rgntNm;
    // }
}