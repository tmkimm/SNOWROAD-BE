package com.snowroad.user.service;

import com.snowroad.config.auth.dto.OAuthAttributes;
import com.snowroad.socialLogin.domain.SocialLogin;
import com.snowroad.socialLogin.domain.SocialLoginRepository;
import com.snowroad.user.domain.*;
import com.snowroad.userContact.domain.UserContact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SocialLoginRepository socialLoginRepository;

    @Transactional
    public User createUserByOAuthAttributes(OAuthAttributes attributes) {
        // User 객체 생성
        User user = User.builder()
                .nickname("nickName")
                .role(Role.USER)
                .build();

        // UserContact 객체 생성
        UserContact userContact = UserContact.builder()
                .userAccountNo(user.getUserAccountNo())
                .email(attributes.getEmail())
                .userName(attributes.getName())
                .build();

        // SocialLogin 객체 생성
        SocialLogin socialLogin =SocialLogin.builder()
                .socialId(attributes.getSocialLoginId())
                .socialLoginProviderCode(attributes.getSocialLoginProviderCode())
                .build();

        // 연관관계 설정
        user.setUserContact(userContact);
        user.setSocialLogin(socialLogin);

        // User 저장 -> Cascade.ALL 덕분에 UserContact, SocialLogin도 자동 저장됨
        return userRepository.save(user);
    }

    public Optional<User> findUserBySocialId(String socialId) {
        return socialLoginRepository.findBySocialId(socialId)
                .map(SocialLogin::getUser);
    }
}
