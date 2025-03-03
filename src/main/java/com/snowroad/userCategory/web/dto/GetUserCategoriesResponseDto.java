package com.snowroad.userCategory.web.dto;

import com.snowroad.event.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class GetUserCategoriesResponseDto {

    @Schema(description = "사용자의 관심 카테고리 리스트")
    private List<Category> data;

    public GetUserCategoriesResponseDto(List<Category> data) {
        this.data = data;
    }
}