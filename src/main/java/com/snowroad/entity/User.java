package com.snowroad.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snowroad.common.domain.BaseTimeEntity;
import com.snowroad.event.domain.Category;
import com.snowroad.user.domain.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_USER_M")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ACNT_NO", nullable = false)
    private Long userAccountNo;

    @Column(name = "DELT_YN", nullable = false)
    private String deleteYn;  // 삭제 여부 (기본값 'N')

    @Column(name = "DATA_DELT_DTTM")
    private LocalDateTime deleteDate;  // 데이터 삭제 일시

    @CreatedDate    // 생성될 시간이 자동 저장된다.
    @Column(name = "USER_JOIN_DTTM")
    private LocalDateTime joinDate;  // 사용자 가입 일시

    @Enumerated(EnumType.STRING)    // 문자열로 저장되도록 설정(숫자면 의미를 알 수 없음)
    @Column(name="USER_ROLE", nullable = false)
    private Role role;

    @Column(name = "USER_NCKN", length = 200)
    private String nickname;  // 사용자가 설정한 닉네임

    // UserCntc (1:1)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserContact userContact;

    // SocialLogins (1:N)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private SocialLogin socialLogin;

    // 사용자 관심 카테고리
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserCategory> userCategories = new ArrayList<>();

    @Column(name = "JOIN_YN", nullable = false)
    private String joinYn = "N";

    // 관심 카테고리 추가 메서드
    public void addCategory(Category category) {
        UserCategory userCategory = UserCategory.builder()
                .user(this)
                .category(category)
                .build();
        this.userCategories.add(userCategory);
    }

    @Builder
    public User(String nickname, Role role) {
        this.nickname = nickname;
        this.role = role;
        this.deleteYn = "N";
    }

    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    // 연관관계 편의 메서드
    public void setUserContact(UserContact userContact) {
        this.userContact = userContact;
        userContact.setUser(this);
    }

    public void setSocialLogin(SocialLogin socialLogin) {
        this.socialLogin = socialLogin;
        socialLogin.setUser(this);
    }
    public String getRoleKey() {
        return this.role.getKey();
    }
}