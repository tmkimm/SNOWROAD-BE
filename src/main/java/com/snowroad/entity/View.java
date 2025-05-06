package com.snowroad.entity;

import com.snowroad.common.domain.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="TB_EVNT_VIEW_D")
// 원칙) Entity는 setter를 만들지 않고 목적과 의도를 알 수 있는 메소드를 생성한다.(예시 : cancleOrder)
public class View extends BaseTimeEntity {

    @Schema(description = "이벤트ID")
    @Column(name = "EVNT_ID")
    @Id
    private Long eventId;

    @Schema(description = "조회수")
    @Column(name = "VIEW_NMVL")
    private int viewNwvl;

    @Builder
    public View(Long eventId, int viewNwvl) {
        this.eventId = eventId;
        this.viewNwvl = viewNwvl;
    }

    public void update(Long eventId, int viewNwvl) {
        this.eventId = eventId;
        this.viewNwvl = viewNwvl;
    }
}
