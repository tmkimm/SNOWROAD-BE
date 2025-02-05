package com.snowroad.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ✅ JWT에서 userId를 추출한 후 DB에서 사용자 정보를 조회하는 로직
        if (!"testUser".equals(username)) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User.withUsername(username)
                .password("{noop}password") // 비밀번호는 JWT 사용 시 필요 없음
                .roles("USER")
                .build();
    }
}
