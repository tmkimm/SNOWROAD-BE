package com.snowroad.web;

import com.snowroad.domain.events.Events;
import com.snowroad.domain.events.EventsRepository;
import com.snowroad.web.dto.EventsSaveRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventsRepository eventsRepository;

    @AfterEach
    public void tearDown() throws Exception {
        eventsRepository.deleteAll();
    }

    @Test
    void Events_등록된다() {
        //given
        String eventNm = "Summer Sale";
        String eventCntn = "Discounts on all summer items.";
        String eventAddr = "123 Summer St, Beach City";
        String operStatDt = "20240101";
        String operEndDt = "20240131";
        String operDttmCntn = "Mon-Fri, 9 AM to 6 PM";
        String ctgyId = "SUMMER2024";
        String ppstEnbnTypeCd = "POPUP";
        Double addrLttd = 34.0522;  // 위도 (예: LA)
        Double addrLotd = -118.2437;  // 경도 (예: LA)
        EventsSaveRequestDto requestDto = EventsSaveRequestDto.builder()
                .eventNm(eventNm)
                .eventCntn(eventCntn)
                .eventAddr(eventAddr)
                .operStatDt(operStatDt)
                .operEndDt(operEndDt)
                .operDttmCntn(operDttmCntn)
                .ctgyId(ctgyId)
                .ppstEnbnTypeCd(ppstEnbnTypeCd)
                .addrLttd(addrLttd)
                .addrLotd(addrLotd)
                .build();
        String url = "http://localhost:"+port+"/api/admin/events";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Events> all = eventsRepository.findAll();
        Events events = all.get(0);
        assertThat(events.getEventNm()).isEqualTo(eventNm);
        assertThat(events.getEventAddr()).isEqualTo(eventAddr);
    }
    @Test
    void Events_수정된다() {
        //given
        String eventNm = "Summer Sale";
        String eventCntn = "Discounts on all summer items.";
        String eventAddr = "123 Summer St, Beach City";
        String operStatDt = "20240101";
        String operEndDt = "20240131";
        String operDttmCntn = "Mon-Fri, 9 AM to 6 PM";
        String ctgyId = "SUMMER2024";
        String ppstEnbnTypeCd = "POPUP";
        Double addrLttd = 34.0522;  // 위도 (예: LA)
        Double addrLotd = -118.2437;  // 경도 (예: LA)
        Events savedEvents = eventsRepository.save(Events.builder()
                .eventNm(eventNm)
                .eventCntn(eventCntn)
                .eventAddr(eventAddr)
                .operStatDt(operStatDt)
                .operEndDt(operEndDt)
                .operDttmCntn(operDttmCntn)
                .ctgyId(ctgyId)
                .ppstEnbnTypeCd(ppstEnbnTypeCd)
                .addrLttd(addrLttd)
                .addrLotd(addrLotd)
                .build());
        Long updateId = savedEvents.getEventId();
        String expectedEventNm = "123 Summer Sale";
        String expectedEventCntn = "456 Discounts on all summer items.";

        EventsSaveRequestDto requestDto = EventsSaveRequestDto.builder()
                .eventNm(expectedEventNm)
                .eventCntn(expectedEventCntn)
                .eventAddr(eventAddr)
                .operStatDt(operStatDt)
                .operEndDt(operEndDt)
                .operDttmCntn(operDttmCntn)
                .ctgyId(ctgyId)
                .ppstEnbnTypeCd(ppstEnbnTypeCd)
                .addrLttd(addrLttd)
                .addrLotd(addrLotd)
                .build();
        String url = "http://localhost:"+port+"/api/admin/events/"+updateId;

        HttpEntity<EventsSaveRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Events> all = eventsRepository.findAll();
        Events events = all.get(0);
        assertThat(events.getEventNm()).isEqualTo(expectedEventNm);
        assertThat(events.getEventCntn()).isEqualTo(expectedEventCntn);
    }
}