package com.snowroad.config.auth.dto;

import com.snowroad.user.domain.Role;
import com.snowroad.user.domain.User;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    private String socialLoginId;   // 소셜 로그인 고유 ID

    private String socialLoginProviderCode; // 소셜로그인제공자구분코드


    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String socialLoginId, String socialLoginProviderCode) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.socialLoginId = socialLoginId;
        this.socialLoginProviderCode = socialLoginProviderCode;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributesName,
                                     Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        else if("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        return ofGoogle(userNameAttributesName, attributes);
    }

    // Google OAuth2User에서 반환하는 사용자 정보를 Map으로 변환
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .socialLoginId((String)attributes.get(userNameAttributeName))
                .socialLoginProviderCode("google")
                .build();
    }

    private static OAuthAttributes ofNaver(String
    userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .socialLoginId((String) response.get(userNameAttributeName))
                .socialLoginProviderCode("naver")
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>)response.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) profile.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .socialLoginId(String.valueOf(attributes.get(userNameAttributeName)))
                .socialLoginProviderCode("kakao")
                .build();
    }
    // User Entity 생성
    public User toEntity() {
        return User.builder()
                .name(name)
                .picture(picture)
                .role(Role.USER)
                .email(email)
                .socialLoginId(socialLoginId)
                .socialLoginProviderCode(socialLoginProviderCode)
                .build();
    }
}
