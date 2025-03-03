package com.snowroad.userCategory.web;


import com.snowroad.event.domain.Category;
import com.snowroad.userCategory.service.UserCategoryService;
import com.snowroad.userCategory.web.dto.GetUserCategoriesResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "사용자 관심 카테고리 API", description = "사용자의 관심 카테고리 관리 API")
@RestController
@RequestMapping("/api/user-categories")
@RequiredArgsConstructor
public class UserCategoryController {

    private final UserCategoryService userCategoryService;

    @Operation(summary = "사용자의 관심 카테고리 조회", description = "사용자의 관심 카테고리를 리스트 형태로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GetUserCategoriesResponseDto.class)))
    @GetMapping("/{userId}")
    public GetUserCategoriesResponseDto getUserCategories(@PathVariable Long userId) {
        return userCategoryService.getUserCategories(userId);
    }

    @Operation(summary = "관심 카테고리 여러 개 추가", description = "사용자의 관심 카테고리를 한 번에 여러 개 추가합니다. 기존 카테고리는 유지됩니다.")
    @PostMapping("/{userId}")
    public ResponseEntity<String> addUserCategories(@PathVariable Long userId, @RequestBody Set<Category> categories) {
        userCategoryService.addUserCategories(userId, categories);
        return ResponseEntity.ok("관심 카테고리가 추가되었습니다.");
    }

    @Operation(summary = "관심 카테고리 수정", description = "사용자의 관심 카테고리를 기존 카테고리를 삭제하고 새로운 목록으로 변경합니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserCategories(@PathVariable Long userId, @RequestBody Set<Category> categories) {
        userCategoryService.updateUserCategories(userId, categories);
        return ResponseEntity.ok("관심 카테고리가 수정되었습니다.");
    }
}