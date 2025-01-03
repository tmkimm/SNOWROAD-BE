package com.snowroad.search.map.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeoMapEnum {

    EARTH_RADIUS(6371.01),
    MAP_DISTANCE_STANDARD(2.0);

    private final double rate;
}
