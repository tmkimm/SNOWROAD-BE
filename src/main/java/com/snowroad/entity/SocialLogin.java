package com.snowroad.entity;

import com.snowroad.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "TB_SCLG_M")
public class SocialLogin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCLG_NO", nullable = false)
    private Long socialLoginNo;  // 소셜 로그인 고유 번호 (기본키)

    @OneToOne
    @JoinColumn(name = "USER_ACNT_NO")
    private User user;

    @Column(name = "SCLG_PRVD_SRTN_CD", length = 10)
    private String socialLoginProviderCode;  // 소셜 로그인 제공자 코드

    @Column(name = "SCLG_ID", length = 255)
    private String socialId;        // 미사용

    @Column(name = "DELT_YN", nullable = false)
    private String deleteYn;  // 삭제 여부 (기본값 'N')

    @Column(name = "DATA_DELT_DTTM")
    private LocalDateTime deleteDate;  // 데이터 삭제 일시
    // UserContact 엔티티
    public void setUser(User user) {
        this.user = user;
    }
    @Builder
    public SocialLogin(User user, String socialId, String socialLoginProviderCode) {
        this.socialId = socialId;
        this.user = user;
        this.socialLoginProviderCode = socialLoginProviderCode;
        this.deleteYn = "N";
    }

}
