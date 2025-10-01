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
public class EventView extends BaseTimeEntity {

    @Schema(description = "이벤트ID")
    @Column(name = "EVNT_ID")
    @Id
    private Long eventId;

    @OneToOne
    @MapsId // Event의 PK를 그대로 사용
    @JoinColumn(name = "EVNT_ID")
    private Events event;

    @Schema(description = "조회수")
    @Column(name = "VIEW_NMVL")
    private int viewNmvl;


    @Builder
    public EventView(int viewNmvl) {
        this.viewNmvl = viewNmvl;
    }

    protected void setEvent(Events event) {
        this.event = event;
        this.eventId = event.getEventId();
    }

    public void updateViewCount(int viewNmvl) {
        this.viewNmvl = viewNmvl;
    }
}
