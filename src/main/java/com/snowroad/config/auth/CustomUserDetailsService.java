package com.snowroad.config.auth;

import com.snowroad.entity.User;
import com.snowroad.user.domain.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 생성자를 통해 UserRepository 자동 주입
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username 대신 JWT에서 추출한 userId를 사용
        // 이 예에서는 username이 userId로 사용된다고 가정
        Long userId = safelyConvertToLong(username);

        if (userId == null) {
            throw new UsernameNotFoundException("Invalid userId: " + username);
        }

        // DB에서 userId로 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // UserDetails 객체를 반환 (비밀번호는 필요 없으므로 빈 값 또는 {noop}으로 처리)
        return org.springframework.security.core.userdetails.User.withUsername(String.valueOf(user.getUserAccountNo()))
                .password("{noop}") // 비밀번호는 필요 없으므로 {noop} 사용
                .roles(user.getRole().name()) // DB에서 가져온 역할을 설정
                .build();
    }

    public Long safelyConvertToLong(String str) {
        try {
            return Long.parseLong(str);  // String을 Long으로 변환
        } catch (NumberFormatException e) {
            // 변환 실패 시 기본값을 반환 (예: null 또는 0L)
            return null;  // 또는 return 0L;
        }
    }
}
