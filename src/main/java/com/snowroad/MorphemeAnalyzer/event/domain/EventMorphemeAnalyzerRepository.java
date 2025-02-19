package com.snowroad.MorphemeAnalyzer.event.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventMorphemeAnalyzerRepository extends JpaRepository<EventMorphemeAnalyzerDTO, Long> {
    List<EventMorphemeAnalyzerDTO> findByMranCntn(String mranCntn);
}
