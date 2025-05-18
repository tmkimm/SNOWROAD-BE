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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Schema(description = "조회수")
    @Column(name = "VIEW_NMVL")
    private int viewNmvl;

    @Builder
    public EventView(Long eventId, int viewNmvl) {
        this.eventId = eventId;
        this.viewNmvl = viewNmvl;
    }

    public void update(Long eventId, int viewNmvl) {
        this.eventId = eventId;
        this.viewNmvl = viewNmvl;
    }
}
