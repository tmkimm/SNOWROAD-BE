package com.snowroad.domain.marks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    @Query("SELECT p from Mark p ORDER BY p.eventId DESC")
    List<Mark> findAllDesc();
}
