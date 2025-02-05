package com.snowroad.user.domain;

import com.snowroad.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String email;
    @Column
    private String picture;

    @Enumerated(EnumType.STRING)    // 문자열로 저장되도록 설정(숫자면 의미를 알 수 없음)
    @Column(nullable = false)
    private Role role;

    @Column(name = "SCLG_ID", length = 255)
    private String socialLoginId;

    @Column(name = "SCLG_PRVD_SRTN_CD", length = 10)
    private String socialLoginProviderCode;

    @Builder
    public User(String name, String email, String picture, Role role, String socialLoginId, String socialLoginProviderCode) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.socialLoginId = socialLoginId;
        this.socialLoginProviderCode = socialLoginProviderCode;
    }

    public User update(String name, String picture, String email) {
        this.name = name;
        this.picture = picture;
        this.email = email;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}