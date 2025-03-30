package com.snowroad.event.domain;

import lombok.Getter;

@Getter
public enum EventStatus {
    NOT_STARTED("시작 전"),
    IN_PROGRESS("진행 중"),
    EXPIRED("마감");

    private final String description;

    EventStatus(String description) {
        this.description = description;
    }
}