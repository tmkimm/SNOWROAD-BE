package com.snowroad.event.web.dto;

import java.util.List;

// 상세 페이지 로드시 해당 컨텐츠의 상세 데이터 및 인근 리스트를 함께 조회하기 위한 wrapper Dto
public class EventDetailWithNearEvents {

    private EventContentsResponseDto eventDetails;
    private List<HomeEventsResponseDto> nearEvents;

    public EventDetailWithNearEvents(EventContentsResponseDto eventDetails, List<HomeEventsResponseDto> nearEvents) {
        this.eventDetails = eventDetails;
        this.nearEvents = nearEvents;
    }

    public EventContentsResponseDto getEventDetails() {
        return eventDetails;
    }

    public List<HomeEventsResponseDto> getNearEvents() {
        return nearEvents;
    }

}
