package com.snowroad.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "팝업, 전시 등록 DTO")
public class EventsSaveRequestDto {
    @Schema(description = "이벤트명")
    private String eventNm;
    @Schema(description = "이벤트내용")
    private String eventCntn;
    @Schema(description = "이벤트주소")
    private String eventAddr;
    @Schema(description = "운영시작일자")
    private String operStatDt;
    @Schema(description = "운영종료일자")
    private String operEndDt;
    @Schema(description = "운영시간내용")
    private String operDttmCntn;
    @Schema(description = "카테고리 ID")
    private String ctgyId;
    @Schema(description = "팝업, 전시 구분 코드")
    private String ppstEnbnTypeCd;
    @Schema(description = "위도", example = "126.9780")
    private double addrLttd;
    @Schema(description = "경도", example = "37.5665")
    private double addrLotd;



//    public Posts toEntity() {
//        return Posts.builder().title(title).content(content).author(author).build();
//    }
}


//curl -X POST "http://localhost:8080/api/event/12345/files" \
//        -F "files=@/path/to/image1.jpg" \
//        -F "files=@/path/to/image2.jpg" \
//        -F "mainImage=@/path/to/mainImage.jpg"

