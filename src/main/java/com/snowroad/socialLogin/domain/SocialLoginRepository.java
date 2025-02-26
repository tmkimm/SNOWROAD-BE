package com.snowroad.socialLogin.domain;

import com.snowroad.entity.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
    Optional<SocialLogin> findBySocialId(String socialId);
}
