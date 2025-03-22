package com.snowroad.mark.web.dto;

import com.snowroad.entity.Mark;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "팝업, 전시 즐겨찾기 등록 DTO")
public class MarkSaveRequestDto {

//    @Schema(description = "사용자계정번호")
//    private Long userAcntNo;  // @Id 및 @GeneratedValue 제거

    @Schema(description = "이벤트ID")
    private Long eventId;

    @Schema(description = "좋아요여부")
    private String likeYn;

    @Builder
    public MarkSaveRequestDto(Long userAcntNo, Long eventId, String likeYn) {
//        this.userAcntNo = userAcntNo;
        this.eventId = eventId;
        this.likeYn = likeYn;
    }

    public Mark toEntity() {
        return Mark.builder()
//                .userAcntNo(userAcntNo)
                .eventId(eventId)
                .likeYn(likeYn)
                .build();
    }
}
