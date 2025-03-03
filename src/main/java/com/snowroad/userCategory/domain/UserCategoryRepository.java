package com.snowroad.userCategory.domain;

import com.snowroad.entity.User;
import com.snowroad.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    List<UserCategory> findByUser(User user);

    long countByUser(User user);     // 사용자의 관심 카테고리 개수 확인
}