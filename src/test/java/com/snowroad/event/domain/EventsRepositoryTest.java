package com.snowroad.event.domain;

import com.snowroad.entity.Events;
import com.snowroad.event.domain.EventsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EventsRepositoryTest {

    @Autowired
    EventsRepository eventsRepository;
    @AfterEach
    public void cleanup() {
        eventsRepository.deleteAll();
    }

    @Test
    public void 이벤트저장_불러오기() {
        //given
        String eventNm = "Summer Sale";
        String eventCntn = "Discounts on all summer items.";
        String eventAddr = "123 Summer St, Beach City";
        String operStatDt = "20240101";
        String operEndDt = "20240131";
        String operDttmCntn = "Mon-Fri, 9 AM to 6 PM";
        String ctgyId = "SUMMER2024";
        String eventTypeCd = "POPUP";
        Double addrLttd = 34.0522;  // 위도 (예: LA)
        Double addrLotd = -118.2437;  // 경도 (예: LA)
        LocalDateTime now = LocalDateTime.of(2024, 12 ,9, 0, 0, 0);
        eventsRepository.save(Events.builder()
                        .eventNm(eventNm)
                        .eventCntn(eventCntn)
                        .eventAddr(eventAddr)
                        .operStatDt(operStatDt)
                        .operEndDt(operEndDt)
                        .operDttmCntn(operDttmCntn)
                        .ctgyId(ctgyId)
                        .eventTypeCd(eventTypeCd)
                        .addrLttd(addrLttd)
                        .addrLotd(addrLotd)
                .build());
        // when
        List<Events> eventsList = eventsRepository.findAll();

        // then
        Events events = eventsList.get(0);
        System.out.println(">>> createdDate : " + events.getCreatedDate());
        assertThat(events.getEventNm()).isEqualTo(eventNm);
        assertThat(events.getEventAddr()).isEqualTo(eventAddr);
        assertThat(events.getCreatedDate()).isAfter(now);
        assertThat(events.getModifiedDate()).isAfter(now);
    }
}