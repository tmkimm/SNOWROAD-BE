package com.snowroad.file.domain.eventFilesDtl;

import com.snowroad.entity.EventFilesDtl;
import com.snowroad.entity.EventFilesMst;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventFilesDtlRepository extends JpaRepository<EventFilesDtl, Long> {
    boolean existsByFileMst(EventFilesMst fileMst);
}
