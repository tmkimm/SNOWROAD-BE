package com.snowroad.web.dto;

import com.snowroad.domain.marks.Mark;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "팝업, 전시 즐겨찾기 등록 DTO")
public class MarkSaveRequestDto {

    @Schema(description = "사용자계정번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAcntNo;

    @Schema(description = "이벤트ID")
    private String eventId;
    @Schema(description = "좋아요여부")
    private String likeYn;

    @Builder
    public MarkSaveRequestDto(String eventId, String likeYn) {
        this.userAcntNo = userAcntNo;
        this.eventId = eventId;
        this.likeYn = likeYn;
    }

    public Mark toEntity() {
        return Mark.builder()
                .userAcntNo(userAcntNo)
                .eventId(eventId)
                .likeYn(likeYn)
                .build();
    }
}
