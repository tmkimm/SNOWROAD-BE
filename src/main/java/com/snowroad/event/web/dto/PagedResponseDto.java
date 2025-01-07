package com.snowroad.event.web.dto;

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
public class PagedResponseDto<T> {
    @Schema(description = "페이지 데이터")
    private List<T> data;  // 데이터 목록

    @Schema(description = "전체 데이터 수")
    private long total;  // 전체 데이터 수
}
