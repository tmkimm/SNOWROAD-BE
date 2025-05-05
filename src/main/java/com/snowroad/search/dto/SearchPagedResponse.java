package com.snowroad.search.dto;

import com.snowroad.entity.Events;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchPagedResponse {

    @Schema(description = "이벤트 List 데이터")
    private List<Events> events;

    @Schema(description = "전체 페이지수")
    private long totalPageCount;

    @Schema(description = "전체 건수")
    private long totalCount;
}
