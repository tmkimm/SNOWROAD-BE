package com.snowroad.search.list.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchListRepository extends JpaRepository<SearchListResponseDTO, Long> {
    List<SearchListResponseDTO> findByEvntId(Long evntId);
}
