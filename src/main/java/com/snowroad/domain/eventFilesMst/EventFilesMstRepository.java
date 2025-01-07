package com.snowroad.domain.eventFilesMst;

import com.snowroad.domain.eventFilesDtl.EventFilesDtl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventFilesMstRepository extends JpaRepository<EventFilesMst, Long> {
}
