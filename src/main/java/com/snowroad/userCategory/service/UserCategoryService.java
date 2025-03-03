package com.snowroad.userCategory.service;

import com.snowroad.entity.User;
import com.snowroad.entity.UserCategory;
import com.snowroad.event.domain.Category;
import com.snowroad.user.domain.UserRepository;
import com.snowroad.userCategory.domain.UserCategoryRepository;
import com.snowroad.userCategory.web.dto.GetUserCategoriesResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCategoryService {

    private final UserCategoryRepository userCategoryRepository;
    private final UserRepository userRepository;

    /**
     * 사용자의 관심 카테고리 조회
     */
    @Transactional(readOnly = true)
    public GetUserCategoriesResponseDto getUserCategories(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));
        List<Category> categories = userCategoryRepository.findByUser(user)
                .stream()
                .map(UserCategory::getCategory)  // ✅ UserCategory에서 Category만 추출
                .collect(Collectors.toList());
        return new GetUserCategoriesResponseDto(categories);
    }
    /**
     * 관심 카테고리 여러 개 추가 (최대 4개 제한)
     */
    @Transactional
    public void addUserCategories(Long userId, Set<Category> categories) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        long existingCount = userCategoryRepository.countByUser(user);
        if (existingCount + categories.size() > 4) {
            throw new IllegalStateException("사용자는 최대 4개의 관심 카테고리만 설정할 수 있습니다.");
        }

        List<UserCategory> userCategoryList = categories.stream()
                .map(category -> UserCategory.builder().user(user).category(category).build())
                .collect(Collectors.toList());

        userCategoryRepository.saveAll(userCategoryList);
    }

    /**
     * 관심 카테고리 수정 (기존 카테고리 삭제 후 새로운 목록 저장)
     */
    @Transactional
    public void updateUserCategories(Long userId, Set<Category> categories) {
        if (categories.size() > 4) {
            throw new IllegalStateException("사용자는 최대 4개의 관심 카테고리만 설정할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 기존 관심 카테고리 삭제
        userCategoryRepository.deleteAll(userCategoryRepository.findByUser(user));

        // 새로운 관심 카테고리 추가
        List<UserCategory> userCategoryList = categories.stream()
                .map(category -> UserCategory.builder().user(user).category(category).build())
                .collect(Collectors.toList());

        userCategoryRepository.saveAll(userCategoryList);
    }
}