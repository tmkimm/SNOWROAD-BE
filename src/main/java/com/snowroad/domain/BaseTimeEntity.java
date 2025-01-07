package com.snowroad.domain;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass   // JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 인식하도록 한다.
@EntityListeners(AuditingEntityListener.class)
// 모든 Entity의 상위 클래스가 되어 Entity들의 createdDate, modifiedDate를 자동으로 관리하는 역할을 한다.
public abstract class BaseTimeEntity {

    @CreatedDate    // 생성될 시간이 자동 저장된다.
    @Column(name="DATA_CRTN_DTTM")
    private LocalDateTime createdDate;

    @LastModifiedDate   // 변경된 시간이 자동 저장된다.
    @Column(name="DATA_EDIT_DTTM")
    private LocalDateTime modifiedDate;
}
