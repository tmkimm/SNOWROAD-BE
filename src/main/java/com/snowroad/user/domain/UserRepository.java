package com.snowroad.user.domain;

import com.snowroad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.socialLogin WHERE u.userAccountNo = :userId")
    Optional<User> findUserWithSocialLogin(@Param("userId") Long userId);
}
