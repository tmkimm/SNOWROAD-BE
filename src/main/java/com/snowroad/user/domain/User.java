package com.snowroad.user.domain;

import com.snowroad.common.domain.BaseTimeEntity;
import com.snowroad.socialLogin.domain.SocialLogin;
import com.snowroad.userContact.domain.UserContact;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_USER_M")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ACNT_NO", nullable = false)
    private Long userAccountNo;

    @Column(name = "DELT_YN", nullable = false, columnDefinition = "varchar(1) default 'N'")
    private String deleteYn;  // 삭제 여부 (기본값 'N')

    @Column(name = "DATA_DELT_DTTM", nullable = false)
    private LocalDateTime deleteDate;  // 데이터 삭제 일시

    @Enumerated(EnumType.STRING)    // 문자열로 저장되도록 설정(숫자면 의미를 알 수 없음)
    @Column(name="USER_ROLE", nullable = false)
    private Role role;

    @Column(name = "USER_NCKN", nullable = false, length = 200)
    private String nickname;  // 사용자가 설정한 닉네임

    // UserCntc (1:1)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserContact userContact;

    // SocialLogins (1:N)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private SocialLogin socialLogin;
    @Builder
    public User(String nickname, Role role) {
        this.nickname = nickname;
        this.role = role;
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