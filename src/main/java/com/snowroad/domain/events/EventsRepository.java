package com.snowroad.domain.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long> {

    @Query("SELECT p from Events p ORDER BY p.eventId DESC")
    List<Events> findAllDesc();
}
