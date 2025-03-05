package com.snowroad.userCategory.service;

import com.snowroad.common.exception.BadRequestException;
import com.snowroad.entity.User;
import com.snowroad.entity.UserCategory;
import com.snowroad.event.domain.Category;
import com.snowroad.user.domain.UserRepository;
import com.snowroad.userCategory.domain.UserCategoryRepository;
import com.snowroad.userCategory.web.dto.UserCategoriesResponseDto;
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
    public UserCategoriesResponseDto getUserCategories(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));
        List<Category> categories = userCategoryRepository.findByUser(user)
                .stream()
                .map(UserCategory::getCategory)  // ✅ UserCategory에서 Category만 추출
                .collect(Collectors.toList());
        return new UserCategoriesResponseDto(categories);
    }
    /**
     * 관심 카테고리 여러 개 추가 (최대 4개 제한)
     */
    @Transactional
    public void addUserCategories(Long userId, Set<Category> categories) {
        User user = findUserById(userId);

        // 사용자가 이미 등록한 카테고리 조회
        Set<Category> existingCategories = getExistingCategories(user);

        // 새로 추가할 카테고리에서 중복 제거
        Set<Category> newCategories = categories.stream()
                .filter(category -> !existingCategories.contains(category))
                .collect(Collectors.toSet());

        // 전체 카테고리 수가 제한을 넘는지 체크
        if (existingCategories.size() + newCategories.size() > 4) {
            throw new BadRequestException("사용자는 최대 4개의 관심 카테고리만 설정할 수 있습니다.");
        }

        saveUserCategories(user, newCategories);
    }

    @Transactional
    public void updateUserCategories(Long userId, Set<Category> categories) {
        // 수정 시에는 전달받은 카테고리 목록만 저장하므로 개수 체크만 수행
        if (categories.size() > 4) {
            throw new BadRequestException("사용자는 최대 4개의 관심 카테고리만 설정할 수 있습니다.");
        }

        User user = findUserById(userId);

        // 기존 카테고리 삭제 (Repository에 deleteByUser 메서드가 있다면 활용)
        userCategoryRepository.deleteAll(getUserCategoryEntities(user));

        // 새로운 관심 카테고리 저장
        saveUserCategories(user, categories);
    }

    // 사용자 조회를 공통 처리
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));
    }

    // 사용자의 기존 카테고리 키를 조회
    private Set<Category> getExistingCategories(User user) {
        return getUserCategoryEntities(user).stream()
                .map(UserCategory::getCategory)
                .collect(Collectors.toSet());
    }

    // 사용자의 UserCategory 엔티티 리스트를 조회 (삭제 용도)
    private List<UserCategory> getUserCategoryEntities(User user) {
        return userCategoryRepository.findByUser(user);
    }

    // User와 카테고리 Set을 받아서 UserCategory 엔티티들을 저장
    private void saveUserCategories(User user, Set<Category> categories) {
        List<UserCategory> userCategoryList = categories.stream()
                .map(category -> UserCategory.builder()
                        .user(user)
                        .category(category)
                        .build())
                .collect(Collectors.toList());
        userCategoryRepository.saveAll(userCategoryList);
    }

}