package com.snowroad.userContact.domain;

import com.snowroad.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_USER_CNTC_M")
public class UserContact {
    @Id
    @Column(name = "USER_ACNT_NO")
    private Long userAccountNo;  // User 엔티티의 PK와 동일한 값 사용

    @OneToOne
    @MapsId  // User의 PK를 그대로 사용
    @JoinColumn(name = "USER_ACNT_NO")
    private User user;

    @Column(name = "USER_EMAIL", length = 255)
    private String userEmail;  // 사용자의 이메일

    @Column(name = "USER_NM", length = 200)
    private String userName;  // 사용자의 이름 (닉네임)

    @Column(name = "USER_TPNO", length = 20)
    private String userPhoneNumber;  // 사용자의 전화번호

    @Column(name = "DELT_YN", nullable = false, columnDefinition = "varchar(1) default 'N'")
    private String deleteYn;  // 삭제 여부 (기본값 'N')

    @Column(name = "DATA_DELT_DTTM", nullable = false)
    private LocalDateTime deleteDate;  // 데이터 삭제 일시

    // UserContact 엔티티
    public void setUser(User user) {
        this.user = user;
    }
    @Builder
    public UserContact(Long userAccountNo, String email, String userName) {
        this.userAccountNo = userAccountNo;
        this.userEmail = email;
        this.userName = userName;
    }
}
