package com.snowroad.event.domain;

import com.snowroad.common.domain.BaseTimeEntity;
import com.snowroad.file.domain.eventFilesMst.EventFilesMst;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="TB_EVNT_M")
// 원칙) Entity는 setter를 만들지 않고 목적과 의도를 알 수 있는 메소드를 생성한다.(예시 : cancleOrder)
public class Events extends BaseTimeEntity {
    @Schema(description = "이벤트ID")
    @Column(name = "EVNT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long eventId;

    @Schema(description = "이벤트명")
    @Column(name = "EVNT_NM", length = 300, nullable = false)
    private String eventNm;

    @Schema(description = "이벤트내용")
    @Column(name = "EVNT_CNTN", columnDefinition = "TEXT", nullable = false)
    private String eventCntn;

    @Schema(description = "이벤트주소")
    @Column(name = "EVNT_ADDR", length = 500, nullable = false)
    private String eventAddr;

    @Schema(description = "운영시작일자")
    @Column(name = "OPER_STAT_DT", length = 8, nullable = false)
    private String operStatDt;

    @Schema(description = "운영종료일자")
    @Column(name = "OPER_END_DT", length = 8, nullable = false)
    private String operEndDt;

    @Schema(description = "운영시간내용")
    @Column(name = "OPER_DTTM_CNTN", length = 200)
    private String operDttmCntn;

    @Schema(description = "카테고리 ID")
    @Column(name = "CTGY_ID", length = 20, nullable = false)
    private String ctgyId;

    @Schema(description = "팝업, 전시 구분 코드")
    @Column(name = "EVNT_TYPE_CD", length = 10, nullable = false)
    private String ppstEnbnTypeCd;

    @Schema(description = "위도")
    @Column(name = "ADDR_LTTD", nullable = false)
    private Double addrLttd;    // TODO 추후 POINT 타입으로 변경

    @Schema(description = "경도")
    @Column(name = "ADDR_LOTD", nullable = false)
    private Double addrLotd;

    @Schema(description = "삭제유무")
    @Column(name = "DELT_YN", length = 10, nullable = false)
    private String deleteYn = "N";

    @Schema(description = "법정동코드")
    @Column(name = "LDCD", length = 30)
    private String ldcd;
    @OneToOne
    @JoinColumn(name = "TUMB_FILE_ID")
    private EventFilesMst eventTumbfile; // EventFilesMst와 1:1 관계 설정

    @OneToOne
    @JoinColumn(name = "EVNT_FILE_ID")
    private EventFilesMst eventFiles; // EventFilesMst와 1:1 관계 설정

    @Builder
    public Events(Long eventId, String eventNm, String eventCntn, String eventAddr, String operStatDt, String operEndDt, String operDttmCntn, String ctgyId, String ppstEnbnTypeCd, Double addrLttd, Double addrLotd, String ldcd) {
        this.eventId = eventId;
        this.eventNm = eventNm;
        this.eventCntn = eventCntn;
        this.eventAddr = eventAddr;
        this.operStatDt = operStatDt;
        this.operEndDt = operEndDt;
        this.operDttmCntn = operDttmCntn;
        this.ctgyId = ctgyId;
        this.ppstEnbnTypeCd = ppstEnbnTypeCd;
        this.addrLttd = addrLttd;
        this.addrLotd = addrLotd;
        this.ldcd = ldcd;
    }

    public void update(String eventNm, String eventCntn, String eventAddr, String operStatDt, String operEndDt, String operDttmCntn, String ctgyId, String ppstEnbnTypeCd, Double addrLttd, Double addrLotd) {
        this.eventNm = eventNm;
        this.eventCntn = eventCntn;
        this.eventAddr = eventAddr;
        this.operStatDt = operStatDt;
        this.operEndDt = operEndDt;
        this.operDttmCntn = operDttmCntn;
        this.ctgyId = ctgyId;
        this.ppstEnbnTypeCd = ppstEnbnTypeCd;
        this.addrLttd = addrLttd;
        this.addrLotd = addrLotd;
    }

    public void updateTumbFile(EventFilesMst eventFilesMst) {
        this.eventTumbfile = eventFilesMst;
    }

    public void updateEventFile(EventFilesMst eventFilesMst) {
        this.eventFiles = eventFilesMst;
    }
}
