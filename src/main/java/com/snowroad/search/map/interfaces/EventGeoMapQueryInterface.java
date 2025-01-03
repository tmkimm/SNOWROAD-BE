package com.snowroad.search.map.interfaces;

import com.snowroad.search.map.dto.EventsGeoMapDto;

import java.util.List;

public interface EventGeoMapQueryInterface {
    List<EventsGeoMapDto> getMapList(double addrLttd, double addrLotd);
}
