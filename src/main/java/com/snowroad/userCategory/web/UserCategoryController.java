package com.snowroad.userCategory.web;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.snowroad.common.exception.ForbiddenException;
import com.snowroad.common.exception.UnauthorizedException;
import com.snowroad.common.util.CurrentUser;
import com.snowroad.event.domain.Category;
import com.snowroad.userCategory.service.UserCategoryService;
import com.snowroad.userCategory.web.dto.UserCategoriesResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
                    schema = @Schema(implementation = UserCategoriesResponseDto.class)))
    @GetMapping("/{userId}")
    public UserCategoriesResponseDto getUserCategories(@PathVariable Long userId) {
        return userCategoryService.getUserCategories(userId);
    }

    // TODO - 인가 로직 AOP로 분리
    @Operation(summary = "관심 카테고리 여러 개 추가", description = "사용자의 관심 카테고리를 한 번에 여러 개 추가합니다. 기존 카테고리는 유지됩니다.")
    @PostMapping("/{userId}")
    public ResponseEntity<String> addUserCategories(@PathVariable Long userId, @RequestBody Set<Category> categories, @CurrentUser UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 존재하지 않습니다. 로그인이 필요합니다.");
        }
        if (!userId.toString().equals(userDetails.getUsername())) {
            throw new ForbiddenException("권한이 존재하지 않습니다.");
        }

        userCategoryService.addUserCategories(userId, categories);
        return ResponseEntity.ok("관심 카테고리가 추가되었습니다.");
    }

    @Operation(summary = "관심 카테고리 수정", description = "사용자의 관심 카테고리를 기존 카테고리를 삭제하고 새로운 목록으로 변경합니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserCategories(@PathVariable Long userId, @RequestBody Set<Category> categories, @CurrentUser UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 존재하지 않습니다. 로그인이 필요합니다.");
        }
        if (!userId.toString().equals(userDetails.getUsername())) {
            throw new ForbiddenException("권한이 존재하지 않습니다.");
        }
        userCategoryService.updateUserCategories(userId, categories);
        return ResponseEntity.ok("관심 카테고리가 수정되었습니다.");
    }
    // 잘못된 Enum 값이 들어왔을 때 400 에러 메시지를 반환
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidEnumException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 코드값이 포함되어 있습니다. 올바른 값을 입력해주세요.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청이 잘못되었습니다.");
    }
}