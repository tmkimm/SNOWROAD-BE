package com.snowroad.MorphemeAnalyzer.event.repository;

import com.snowroad.MorphemeAnalyzer.event.domain.EventNounMran;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventNounMranRepository extends JpaRepository<EventNounMran, Long> {
    List<EventNounMran> findByMranCntn(String mranCntn);
}
