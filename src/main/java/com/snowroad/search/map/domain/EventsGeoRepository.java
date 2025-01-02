package com.snowroad.search.map.domain;

import com.snowroad.domain.events.Events;
import com.snowroad.search.map.dto.EventsGeoMapDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventsGeoRepository extends JpaRepository<Events, Long> {
    @Query("SELECT addrLttd, addrLotd FROM Events")
    List<EventsGeoMapDto> findEventsGeoCustomPoint(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon
    );
}
