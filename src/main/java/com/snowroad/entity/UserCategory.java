package com.snowroad.entity;

import com.snowroad.event.domain.Category;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_USER_CTGY_M")
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_CTRY_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ACNT_NO", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "CTGY_ID", nullable = false)
    private Category category;

    @Builder
    public UserCategory(User user, Category category) {
        this.user = user;
        this.category = category;
    }

    // 연관관계 편의 메서드
    public void setUser(User user) {
        this.user = user;
        user.getUserCategories().add(this);
    }
}
