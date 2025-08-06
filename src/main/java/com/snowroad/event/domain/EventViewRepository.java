package com.snowroad.event.domain;

import com.snowroad.entity.View; // EventView 엔티티 임포트
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventViewRepository extends JpaRepository<View, Long> {
    // EventView 엔티티의 기본 CRUD 작업을 위한 메서드가 자동으로 제공됩니다.
    // 필요에 따라 추가적인 쿼리 메서드를 여기에 정의할 수 있습니다.
}
