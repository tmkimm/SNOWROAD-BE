package com.snowroad.search.dto;

import com.snowroad.search.entity.PopularSearch;
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
public class PopularSearchResponse {

    @Schema(description = "인기검색목록")
    private List<PopularSearch> events;
}
