package com.snowroad.config.auth.dto;

import com.snowroad.domain.user.Role;
import com.snowroad.domain.user.User;
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

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributesName,
                                     Map<String, Object> attributes) {
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
                .build();
    }

    // User Entity 생성
    public User toEntity() {
        return User.builder()
                .name(name)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
