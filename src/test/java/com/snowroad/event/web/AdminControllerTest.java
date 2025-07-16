package com.snowroad.event.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowroad.entity.Events;
import com.snowroad.event.domain.EventsRepository;
import com.snowroad.event.web.dto.EventsResponseDto;
import com.snowroad.event.web.dto.EventsSaveRequestDto;
import com.snowroad.event.web.dto.PagedResponseDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {
    @LocalServerPort
    private int port;


    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        eventsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    void Events_등록된다() throws Exception{
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
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString((requestDto)))
        ).andExpect(status().isOk());

        // then
        List<Events> all = eventsRepository.findAll();
        Events events = all.get(0);
        assertThat(events.getEventNm()).isEqualTo(eventNm);
        assertThat(events.getEventAddr()).isEqualTo(eventAddr);
    }
    @Test
    @WithMockUser(roles="USER")
    void Events_수정된다() throws Exception{
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
        Events savedEvents = eventsRepository.save(Events.builder()
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
                .ppstEnbnTypeCd(eventTypeCd)
                .addrLttd(addrLttd)
                .addrLotd(addrLotd)
                .build();
        String url = "http://localhost:"+port+"/api/admin/events/"+updateId;

        HttpEntity<EventsSaveRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString((requestDto)))
        ).andExpect(status().isOk());

        // then
        List<Events> all = eventsRepository.findAll();
        Events events = all.get(0);
        assertThat(events.getEventNm()).isEqualTo(expectedEventNm);
        assertThat(events.getEventCntn()).isEqualTo(expectedEventCntn);
    }

    @Test
    @WithMockUser(roles="USER")
    void Events_삭제된다() throws Exception{
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
        Events savedEvents = eventsRepository.save(Events.builder()
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
                .ppstEnbnTypeCd(eventTypeCd)
                .addrLttd(addrLttd)
                .addrLotd(addrLotd)
                .build();
        String url = "http://localhost:"+port+"/api/admin/events/"+updateId;

        HttpEntity<EventsSaveRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        mvc.perform(delete(url)).andExpect(status().isOk());

        // then
        Optional<Events> deletedEvent = eventsRepository.findById(updateId);
        assertThat(deletedEvent.isPresent()).isFalse();
    }
    @Test
    @WithMockUser(roles="USER")
    void Events_등록_리스트조회_상세조회_일치한다() throws Exception {
        // given
        String eventNm = "Test Summer Event";
        String eventCntn = "Special discounts for summer.";
        String eventAddr = "456 Test Road, Test City";
        String operStatDt = "20240701";
        String operEndDt = "20240731";
        String operDttmCntn = "Weekdays 10AM-7PM";
        String ctgyId = "SUMMER2025";
        String eventTypeCd = "POPUP";
        Double addrLttd = 37.5665;
        Double addrLotd = 126.9780;

        EventsSaveRequestDto requestDto = EventsSaveRequestDto.builder()
                .eventNm(eventNm)
                .eventCntn(eventCntn)
                .eventAddr(eventAddr)
                .operStatDt(operStatDt)
                .operEndDt(operEndDt)
                .operDttmCntn(operDttmCntn)
                .ctgyId(ctgyId)
                .ppstEnbnTypeCd(eventTypeCd)
                .addrLttd(addrLttd)
                .addrLotd(addrLotd)
                .build();

        String saveUrl = "http://localhost:" + port + "/api/admin/events";

        // when: 이벤트 등록
        mvc.perform(post(saveUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then: 리스트 조회
        String listUrl = "http://localhost:" + port + "/api/admin/events";
        MvcResult listResult = mvc.perform(get(listUrl))
                .andExpect(status().isOk())
                .andReturn();

        String listContent = listResult.getResponse().getContentAsString();
        PagedResponseDto<?> pagedResponse = new ObjectMapper().readValue(listContent, PagedResponseDto.class);

        assertThat(pagedResponse.getData()).isNotNull();
        assertThat(pagedResponse.getData().size()).isGreaterThan(0);

        // 가장 마지막에 추가된 이벤트 ID 확인 (페이지 0 기준)
        Map<String, Object> firstItem = (Map<String, Object>) pagedResponse.getData().get(0);
        Long savedEventId = Long.valueOf(firstItem.get("eventId").toString());

        // 상세 조회
        String detailUrl = "http://localhost:" + port + "/api/admin/events/" + savedEventId;
        MvcResult detailResult = mvc.perform(get(detailUrl))
                .andExpect(status().isOk())
                .andReturn();

        String detailContent = detailResult.getResponse().getContentAsString();
        EventsResponseDto detailDto = new ObjectMapper().readValue(detailContent, EventsResponseDto.class);

        // 상세 조회 값 검증
        assertThat(detailDto.getEventNm()).isEqualTo(eventNm);
        assertThat(detailDto.getEventCntn()).isEqualTo(eventCntn);
        assertThat(detailDto.getEventAddr()).isEqualTo(eventAddr);
        assertThat(detailDto.getCtgyId()).isEqualTo(ctgyId);
    }
}