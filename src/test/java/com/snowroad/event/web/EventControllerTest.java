package com.snowroad.event.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowroad.entity.EventFilesDtl;
import com.snowroad.entity.EventFilesMst;
import com.snowroad.entity.View;
import com.snowroad.entity.Events;
import com.snowroad.event.domain.EventsRepository;
import com.snowroad.event.web.dto.DetailEventsResponseDto;
import com.snowroad.event.web.dto.EventsSaveRequestDto;
import com.snowroad.event.web.dto.PagedResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; // List 사용을 위해 추가
import java.util.Comparator; // 객체간 비교를 위해 추가
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // GET 요청
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.snowroad.file.domain.eventFilesMst.EventFilesMstRepository;
import com.snowroad.file.domain.eventFilesDtl.EventFilesDtlRepository;
import com.snowroad.event.domain.EventViewRepository;
import org.springframework.web.multipart.MultipartFile;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventControllerTest {
    @LocalServerPort
    private int port;

    // ⭐ ObjectMapper를 @Autowired로 주입받아야 합니다.
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired // ✨ EventFilesMstRepository 주입
    private EventFilesMstRepository eventFilesMstRepository;
    @Autowired // ✨ EventFilesDtlRepository 주입
    private EventFilesDtlRepository eventFilesDtlRepository;
    @Autowired // ✨ EventViewRepository 주입
    private EventViewRepository eventViewRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    // ✨ 생성된 이벤트 데이터를 담을 리스트 선언
    // 해당 리스트를 기반으로 조회 테스트 일괄처리
    private List<Events> createdEvents;

    private String filePath = "event-test-images";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // ✨ 각 테스트 전에 5개의 이벤트 데이터를 생성하고 리스트에 저장
        createdEvents = new ArrayList<>(); // 리스트 초기화
        for (int i = 0; i < 5; i++) {
            createdEvents.add(createAndSaveEvent(i, "10")); // 5개 생성
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        eventsRepository.deleteAll();
        eventFilesDtlRepository.deleteAll();
        eventFilesMstRepository.deleteAll();
        eventViewRepository.deleteAll();
    }


    public EventFilesMst saveEventFilesMst() {
        EventFilesMst fileMst = EventFilesMst.builder()
                .build();
        return eventFilesMstRepository.save(fileMst);
    }

    public EventFilesDtl saveEventFilesDtl(EventFilesMst fileMst, String filePath, String fileUrl, MultipartFile file, String fileName) {
        EventFilesDtl fileDtl = EventFilesDtl.builder()
                .fileMst(fileMst)
                .filePath(filePath)
                .fileNm(fileName)
                .origFileNm(file.getOriginalFilename())
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .fileUrl(fileUrl)
                .build();
        return eventFilesDtlRepository.save(fileDtl);
    }


    /**
     * 테스트용 이벤트를 생성하고 저장하는 유틸리티 메서드
     * @param index 이벤트 구분을 위한 인덱스
     * @param eventTypeCd 이벤트 타입 코드
     * @return 저장된 Events 엔티티
     */
    private Events createAndSaveEvent(int index, String eventTypeCd) {

        String eventNm = "테스트 이벤트 " + index;
        String eventCntn = "테스트 이벤트 내용 " + index;
        String eventAddr = "서울시 강남구 테스트로 " + index;
        String rads = "서울시 강남구 테스트로 " + index;
        String lnad = "서울 강남구 역삼동 " + (800 + index);

        // 날짜를 현재 날짜보다 충분히 이후로 설정하여 모든 이벤트가 조회되도록 함
        // 예: 모든 이벤트가 오늘부터 시작하여 며칠 뒤에 종료되도록
        LocalDate startDate = LocalDate.now().plusDays(1 + index); // 현재 날짜 + (1 + index) 일
        LocalDate endDate = LocalDate.now().plusDays(2 + index);   // startDate + 1일
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String operStatDt = startDate.format(formatter);
        String operEndDt = endDate.format(formatter);

        String operDttmCntn = "매일 " + (10 + index) + ":00 ~ " + (18 + index) + ":00";
        String ctgyId = "CATEGORY_" + index;
        Double addrLttd = 37.5 + (0.001 * index);
        Double addrLotd = 127.0 + (0.001 * index);
        String ldcd = "1168010100";
        String eventDetailUrl = "https://www.example.com/test-event-" + index;


        // 1. Events 엔티티 생성 및 저장
        Events newEvent = Events.builder()
                .eventNm(eventNm)
                .eventCntn(eventCntn)
                .eventAddr(eventAddr)
                .rads(rads)
                .lnad(lnad)
                .operStatDt(operStatDt)
                .operEndDt(operEndDt)
                .operDttmCntn(operDttmCntn)
                .ctgyId(ctgyId)
                .eventTypeCd(eventTypeCd)
                .addrLttd(addrLttd)
                .addrLotd(addrLotd)
                .ldcd(ldcd)
                .eventDetailUrl(eventDetailUrl)
                .build();

        Events savedEvents = eventsRepository.save(newEvent);
        System.out.println("생성된 EventId: " + savedEvents.getEventId());

        // 2. EventFilesMst 데이터 생성 및 저장
        EventFilesMst tumbFileMst = EventFilesMst.builder().build(); // ID만 필요하므로 빈 빌더 사용
        tumbFileMst = eventFilesMstRepository.save(tumbFileMst); // EventFilesMst 저장
        System.out.println("생성된 EventFilesMstId: " + tumbFileMst.getFileMstId());

        // 3. Events 엔티티와 EventFilesMst 연결 (updateTumbFile 메소드 활용)
        savedEvents.updateTumbFile(tumbFileMst);

        // 4. EventFilesDtl 데이터 생성 및 저장
        EventFilesDtl tumbFileDtl = EventFilesDtl.builder()
                .fileMst(tumbFileMst) // EventFilesDtl이 EventFilesMst를 참조하도록 설정
                .filePath("/test/event/" + index + "/")
                .fileNm("test_event_" + index + ".jpg")
                .origFileNm("original_test_event_" + index + ".jpg")
                .fileThumbUrl("http://test.image.com/thumb/event_" + index + "_small.jpg")
                .fileSize(10000L + index)
                .fileType("image/jpeg")
                .fileUrl("http://test.image.com/event_" + index + "_main.jpg")
                .build();
        eventFilesDtlRepository.save(tumbFileDtl); // EventFilesDtl 저장
        System.out.println("생성된 EventFilesDtlId: " + tumbFileDtl.getFileDtlId());

        // 2. 이벤트 ID 가져오기
        Long eventId = savedEvents.getEventId(); // ID가 null이면 save 안 된 것!
        System.out.println("eventId 조회: " + eventId);

        // 5. EventView 데이터 생성 및 저장
        View eventView = eventViewRepository.save(View.builder()
                .eventId(eventId)
                .viewNwvl(100 + index)
                .build());
//        View eventView = View.builder()
//                   // 250721 해당 부분에서 getEventId를 제대로 세팅하지 못하는 문제, @Id 어노테이션 때문에 jpa가 인식을...
////                .eventId(savedEvents.getEventId()) // Events의 ID를 EventView의 ID로 설정 (중요!)
//                .viewNwvl(100+index) // 조회수 값 설정
//                .event(savedEvents) // ✨ savedEvents 객체를 builder에 전달
//                .build();
        eventViewRepository.save(eventView); // EventView 저장
        System.out.println("생성된 EventViewId: " + eventView.getEventId()); // EventView의 ID는 Events의 ID와 같음


        // 6. 업데이트된 Events 객체를 다시 저장하여 db 반영
        return eventsRepository.save(savedEvents); // 업데이트된 Events 객체를 다시 저장
    }

    @Test
    @WithMockUser(roles="USER")
    void Events_인기순으로_조회된다() throws Exception {

        // given
        String eventTypeCd = "10";
        String url = "http://localhost:" + port + "/api/main-event/rank/" + eventTypeCd;

        // when
        MvcResult result = mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body (인기순 조회): " + responseBody);

        // JSON 응답을 JsonNode로 파싱하여 eventId만 추출
        JsonNode rootNode = objectMapper.readTree(responseBody);
        List<Long> actualEventIds = new ArrayList<>();

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                // 각 객체에서 "eventId" 필드의 값을 Long 타입으로 추출
                if (node.has("eventId")) {
                    actualEventIds.add(node.get("eventId").asLong());
                }
            }
        }
        System.out.println("Response Body (eventId 추출 완료): " + actualEventIds);

        assertThat(actualEventIds).hasSize(5);

        // ✨ 하드코딩된 예상 조회수 순서를 직접 비교
        // 예상되는 eventId 순서 (가장 큰 값부터 내림차순)
        // @BeforeEach에서 1부터 5까지 생성되었다면, 기대값은 5, 4, 3, 2, 1
        assertThat(actualEventIds.get(0)).isEqualTo(5L);
        assertThat(actualEventIds.get(1)).isEqualTo(4L);
        assertThat(actualEventIds.get(2)).isEqualTo(3L);
        assertThat(actualEventIds.get(3)).isEqualTo(2L);
        assertThat(actualEventIds.get(4)).isEqualTo(1L);

        System.out.println("인기순 조회 테스트 성공: 예상 순서대로 내림차순 정렬");
    }

//    @Test
//    @WithMockUser(roles="USER")
//    void Events_리스트조회순으로_조회된다() throws Exception {
//
//        String eventTypeCd = "10"; // eventTypeCd "10"으로 필터링 (5개 이벤트)
//
//        // --- 1. 조회순 (sortType=10): viewNmvl DESC
//        System.out.println("\n--- 테스트 시작: 조회순 (sortType=10) ---");
//        String urlViewCount = "http://localhost:" + port + "/api/events/list/" + eventTypeCd + "?sortType=10";
//        // when
//        MvcResult resultViewCount = mvc.perform(get(urlViewCount)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseBodyViewCount = resultViewCount.getResponse().getContentAsString();
//        System.out.println("Response Body (조회순): " + responseBodyViewCount);
//
//        PagedResponseDto<DetailEventsResponseDto> pagedResponseViewCount = objectMapper.readValue(
//                responseBodyViewCount,
//                objectMapper.getTypeFactory().constructParametricType(PagedResponseDto.class, DetailEventsResponseDto.class)
//        );
//        // ⭐ 수정 부분: getContent() -> getData()
//        List<DetailEventsResponseDto> actualEventsViewCount = pagedResponseViewCount.getData();
//
//        assertThat(actualEventsViewCount).isNotNull().hasSize(5); // eventTypeCd "10" 5개
//        assertThat(pagedResponseViewCount.getTotal()).isEqualTo(5L); // ⭐ totalElements -> total
//
//        // viewNmvl 내림차순 검증 (109, 108, 107, 106, 105)
//        for (int i = 0; i < actualEventsViewCount.size() - 1; i++) {
//            assertThat(actualEventsViewCount.get(i).getViewNmvl())
//                    .as("ViewNmvl at index %d should be >= ViewNmvl at index %d", i, i + 1)
//                    .isGreaterThanOrEqualTo(actualEventsViewCount.get(i + 1).getViewNmvl());
//        }
//        // 정확한 순서 검증: index가 작을수록 viewNmvl이 크도록 설정했으므로 eventId 1 -> 2 -> 3 -> 4 -> 5 순서 기대
//        assertThat(actualEventsViewCount.stream().map(DetailEventsResponseDto::getEventId).collect(Collectors.toList()))
//                .containsExactly(1L, 2L, 3L, 4L, 5L);
//        System.out.println("조회순 테스트 성공!");
//    }
}