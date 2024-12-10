package com.snowroad.domain.events;

import com.snowroad.domain.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
// 원칙) Entity는 setter를 만들지 않고 목적과 의도를 알 수 있는 메소드를 생성한다.(예시 : cancleOrder)
public class Events extends BaseTimeEntity {
    @Schema(description = "이벤트ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Schema(description = "이벤트명")
    @Column(length = 300, nullable = false)
    private String eventNm;

    @Schema(description = "이벤트내용")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String eventCntn;

    @Schema(description = "이벤트주소")
    @Column(length = 500, nullable = false)
    private String eventAddr;

    @Schema(description = "운영시작일자")
    @Column(length = 8, nullable = false)
    private String operStatDt;

    @Schema(description = "운영종료일자")
    @Column(length = 8, nullable = false)
    private String operEndDt;

    @Schema(description = "운영시간내용")
    @Column(length = 200)
    private String operDttmCntn;

    @Schema(description = "카테고리 ID")
    @Column(length = 20, nullable = false)
    private String ctgyId;

    @Schema(description = "팝업, 전시 구분 코드")
    @Column(length = 10, nullable = false)
    private String ppstEnbnTypeCd;

    @Schema(description = "위도")
    @Column(name = "ADDR_LTTD", nullable = false)
    private Double addrLttd;    // TODO 추후 POINT 타입으로 변경

    @Schema(description = "경도")
    @Column(name = "ADDR_LOTD", nullable = false)
    private Double addrLotd;

    @Builder
    public Events(Long eventId, String eventNm, String eventCntn, String eventAddr, String operStatDt, String operEndDt, String operDttmCntn, String ctgyId, String ppstEnbnTypeCd, Double addrLttd, Double addrLotd) {
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
}
