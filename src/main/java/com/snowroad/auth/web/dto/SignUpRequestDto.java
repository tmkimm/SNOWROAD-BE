package com.snowroad.auth.web.dto;

import com.snowroad.event.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private Long userId;
    private Set<Category> categories; // 관심 카테고리 (비어있을 수도 있음)
}
