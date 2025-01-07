package com.snowroad.domain.marks;

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
public class Mark extends BaseTimeEntity {

    @Schema(description = "사용자계정번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAcntNo;

    @Schema(description = "이벤트ID")
    @Column(length = 20, nullable = false)
    private String eventId;

    @Schema(description = "좋아요여부")
    @Column(length = 1, nullable = false)
    private String likeYn;

    @Builder
    public Mark(Long userAcntNo, String eventId, String likeYn) {
        this.userAcntNo = userAcntNo;
        this.eventId = eventId;
        this.likeYn = likeYn;
    }

    public void update(Long userAcntNo, String eventId, String likeYn) {
        this.userAcntNo = userAcntNo;
        this.eventId = eventId;
        this.likeYn = likeYn;
    }
}
