package com.snowroad.socialLogin.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
    Optional<SocialLogin> findBySocialId(String socialId);
}
