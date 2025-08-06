package com.snowroad.userCategory.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.snowroad.event.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class UserCategoriesResponseDto {

    @Schema(description = "사용자의 관심 카테고리 리스트")
    private List<Category> data;
    @JsonCreator
    public UserCategoriesResponseDto(List<Category> data) {
        this.data = data;
    }
}