package com.snowroad.user.service;

import com.snowroad.auth.web.dto.SignUpRequestDto;
import com.snowroad.common.exception.BadRequestException;
import com.snowroad.config.auth.dto.OAuthAttributes;
import com.snowroad.entity.SocialLogin;
import com.snowroad.entity.User;
import com.snowroad.socialLogin.domain.SocialLoginRepository;
import com.snowroad.user.domain.*;
import com.snowroad.entity.UserContact;
import com.snowroad.userCategory.service.UserCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SocialLoginRepository socialLoginRepository;

    private final UserCategoryService userCategoryService;

    private final NicknameGenerator nicknameGenerator;



    @Transactional
    public User createUserByOAuthAttributes(OAuthAttributes attributes) {
        // User 객체 생성
        User user = User.builder()
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

    @Transactional
    public void signUp(SignUpRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));

        if (user.getJoinYn().equals("Y")) {
            throw new BadRequestException("이미 회원가입된 사용자입니다.");
        }

        String nickname = nicknameGenerator.generate(); // 닉네임 랜덤 생성
        // 회원가입
        user.signUp(nickname);
        // 관심 카테고리 추가
        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            userCategoryService.updateUserCategories(user.getUserAccountNo(), request.getCategories());
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));

        // User만 삭제하면, cascade = CascadeType.ALL, orphanRemoval = true 설정에 의해 UserContact & SocialLogin 자동 삭제됨
        userRepository.delete(user);

    }
}
