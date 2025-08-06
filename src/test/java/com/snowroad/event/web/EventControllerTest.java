package com.snowroad.event.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowroad.config.H2FunctionConfig;
import com.snowroad.config.auth.dto.CustomUserDetails;
import com.snowroad.entity.*;
import com.snowroad.event.domain.EventsRepository;
import com.snowroad.event.web.dto.DetailEventsResponseDto;
import com.snowroad.event.web.dto.EventsSaveRequestDto;
import com.snowroad.event.web.dto.PagedResponseDto;
import com.snowroad.user.domain.Role;
import com.snowroad.user.domain.UserRepository;
import com.snowroad.userCategory.web.dto.UserCategoriesResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.snowroad.event.domain.Category;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; // List 사용을 위해 추가
import java.util.Comparator; // 객체간 비교를 위해 추가
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // GET 요청
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.snowroad.file.domain.eventFilesMst.EventFilesMstRepository;
import com.snowroad.file.domain.eventFilesDtl.EventFilesDtlRepository;
import com.snowroad.event.domain.EventViewRepository;
import org.springframework.web.multipart.MultipartFile;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(H2FunctionConfig.class)
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
    @Autowired // ✨ EventViewRepository 주입
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    // ✨ 생성된 이벤트 데이터를 담을 리스트 선언
    // 해당 리스트를 기반으로 조회 테스트 일괄처리
    private List<Events> createdEvents;

    private String filePath = "event-test-images";
    private User savedUser;

    @BeforeEach
    public void setup() {

        savedUser = userRepository.save(User.builder()
                        .nickname("테스트유저")
                        .role(Role.USER)
                .build());

        CustomUserDetails userDetails = new CustomUserDetails(1L, "testuser", "ROLE_USER", "Y");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // ✨ 각 테스트 전에 5개의 이벤트 데이터를 생성하고 리스트에 저장
        createdEvents = new ArrayList<>(); // 리스트 초기화
        for (int i = 0; i < 5; i++) {
            createdEvents.add(createAndSaveEvent(i, "10")); // 5개 생성
        }

        System.out.println("Every Table Added");
    }

    @AfterEach
    public void tearDown() throws Exception {
        eventsRepository.deleteAll();
        eventFilesDtlRepository.deleteAll();
        eventFilesMstRepository.deleteAll();
        eventViewRepository.deleteAll();
        System.out.println("Every Table Deleted");
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

        // index = 0이면 오늘-5일 / 오늘+1일
        // index = 1이면 오늘-4일 / 오늘+2일
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.now().minusDays(5 - index);
        LocalDate endDate = LocalDate.now().plusDays(1 + index);
        String operStatDt = startDate.format(formatter);
        String operEndDt = endDate.format(formatter);

        String operDttmCntn = "매일 " + (10 + index) + ":00 ~ " + (18 + index) + ":00";
        String ctgyId = "";
        if (index % 2 != 0) {
            ctgyId = "PASH";
        }
        else{
            ctgyId = "SPORT";
        }
        Double addrLttd = 37.5 + (0.001 * index);
        Double addrLotd = 127.0 + (0.001 * index);
        String ldcd = "1168010100";
        String eventDetailUrl = "https://www.example.com/test-event-" + index;
        String deltYn = "N";

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

        // 6. 업데이트된 Events 객체를 다시 저장하여 db 반영
        return eventsRepository.save(savedEvents); // 업데이트된 Events 객체를 다시 저장
    }


    private Events createAndSaveFutureEvent(int index, String eventTypeCd) {

        String eventNm = "테스트 이벤트 " + index;
        String eventCntn = "테스트 이벤트 내용 " + index;
        String eventAddr = "서울시 성동구 테스트로 " + index;
        String rads = "서울시 성동구 테스트로 " + index;
        String lnad = "서울 성동구 성수동1가 " + (800 + index);

        // index = 0이면 오늘-5일 / 오늘+1일
        // index = 1이면 오늘-4일 / 오늘+2일
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.now().plusDays(1 + index);
        LocalDate endDate = LocalDate.now().plusDays(7 + index);
        String operStatDt = startDate.format(formatter);
        String operEndDt = endDate.format(formatter);

        String operDttmCntn = "주말 " + (8 + index) + ":00 ~ " + (18 + index) + ":00";
        String ctgyId = "CATEGORY_" + index;
        Double addrLttd = 37.5 + (0.001 * index);
        Double addrLotd = 127.0 + (0.001 * index);
        String ldcd = "1168010100";
        String eventDetailUrl = "https://www.example2.com/test-event-" + index;
        String deltYn = "N";

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
        // ⭐ MvcResult.getResponse().getContentAsString() 대신 바이트 배열로 가져와 UTF-8로 명시적 디코딩
        String responseBody = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);

        // JSON 응답을 JsonNode로 파싱하여 eventNm만 추출
        JsonNode rootNode = objectMapper.readTree(responseBody);

        List<String> actualEventNms = new ArrayList<>();

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                if (node.has("eventNm")) {
                    actualEventNms.add(node.get("eventNm").asText());
                }
            }
        }
        System.out.println("Response Body (eventNm 추출 완료): " + actualEventNms);

        assertThat(actualEventNms).hasSize(5);

        // ✨ 하드코딩된 예상 조회수 순서를 직접 비교
        // 예상되는 eventId 순서 (가장 큰 값부터 내림차순)
        assertThat(actualEventNms.get(0)).isEqualTo("테스트 이벤트 4");
        assertThat(actualEventNms.get(1)).isEqualTo("테스트 이벤트 3");
        assertThat(actualEventNms.get(2)).isEqualTo("테스트 이벤트 2");
        assertThat(actualEventNms.get(3)).isEqualTo("테스트 이벤트 1");
        assertThat(actualEventNms.get(4)).isEqualTo("테스트 이벤트 0");

        System.out.println("Events_인기순으로_조회된다 테스트 성공");
    }

    @Test
    @WithMockUser(roles="USER")
    void Events_배너_조회된다() throws Exception {

        // given
        String eventTypeCd = "10";
        String url = "http://localhost:" + port + "/api/main-event/banner/" + eventTypeCd;

        // when
        MvcResult result = mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("\n--- 쿼리 실행 완료, H2 콘솔에서 데이터 확인 시작 ---");
        System.out.println("\n--- H2 콘솔 확인 시간 종료 ---");
        // then
        String responseBody = result.getResponse().getContentAsString();

        // JSON 응답을 JsonNode로 파싱하여 eventId만 추출
        JsonNode rootNode = objectMapper.readTree(responseBody);
        List<String> actualEventIds = new ArrayList<>();

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                // 각 객체에서 "eventId" 필드의 값을 Long 타입으로 추출
                if (node.has("operEndDt")) {
                    actualEventIds.add(node.get("operEndDt").asText());
                }
            }
        }
        System.out.println("Response Body (operEndDt 추출 완료): " + actualEventIds);

        assertThat(actualEventIds).hasSize(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate endDate1 = LocalDate.now().plusDays(1);
        LocalDate endDate2 = LocalDate.now().plusDays(2);
        LocalDate endDate3 = LocalDate.now().plusDays(3);
        LocalDate endDate4 = LocalDate.now().plusDays(4);
        LocalDate endDate5 = LocalDate.now().plusDays(5);

        assertThat(actualEventIds.get(0)).isEqualTo(endDate5.format(formatter));
        assertThat(actualEventIds.get(1)).isEqualTo(endDate4.format(formatter));
        assertThat(actualEventIds.get(2)).isEqualTo(endDate3.format(formatter));
        assertThat(actualEventIds.get(3)).isEqualTo(endDate2.format(formatter));
        assertThat(actualEventIds.get(4)).isEqualTo(endDate1.format(formatter));

        System.out.println("Events_배너_조회된다 테스트 성공");
    }

    @Test
    @WithMockUser(roles="USER")
    void Events_마감임박순_조회된다() throws Exception {

        // given
        String eventTypeCd = "10";
        String url = "http://localhost:" + port + "/api/main-events/operEnd/" + eventTypeCd;

        // when
        System.out.println("bf start ========= ");
        MvcResult result = mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("af start ========= ");
        System.out.println(responseBody);

        // JSON 응답을 JsonNode로 파싱하여 eventId만 추출
        JsonNode rootNode = objectMapper.readTree(responseBody);
        List<String> actualEventIds = new ArrayList<>();

        JsonNode dataNode = rootNode.get("data");

        if (dataNode != null && dataNode.isArray()) {
            for (JsonNode node : dataNode) {
                if (node.has("operEndDt")) {
                    actualEventIds.add(node.get("operEndDt").asText());
                }
            }
        }
        System.out.println("Response Body (operEndDt 추출 완료): " + actualEventIds);

        assertThat(actualEventIds).hasSize(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate endDate1 = LocalDate.now().plusDays(1);
        LocalDate endDate2 = LocalDate.now().plusDays(2);
        LocalDate endDate3 = LocalDate.now().plusDays(3);
        LocalDate endDate4 = LocalDate.now().plusDays(4);
        LocalDate endDate5 = LocalDate.now().plusDays(5);

        assertThat(actualEventIds.get(0)).isEqualTo(endDate1.format(formatter));
        assertThat(actualEventIds.get(1)).isEqualTo(endDate2.format(formatter));
        assertThat(actualEventIds.get(2)).isEqualTo(endDate3.format(formatter));
        assertThat(actualEventIds.get(3)).isEqualTo(endDate4.format(formatter));
        assertThat(actualEventIds.get(4)).isEqualTo(endDate5.format(formatter));

        System.out.println("Events_마감임박순_조회된다 테스트 성공");
    }

    @Test
    @WithMockUser(roles="USER")
    void Events_오픈임박순_조회된다() throws Exception {

        // 오픈임박건 날짜 위해 5개의 이벤트 데이터를 추가로 생성하고 리스트에 저장
        createdEvents = new ArrayList<>(); // 리스트 초기화
        for (int i = 0; i < 5; i++) {
            createdEvents.add(createAndSaveFutureEvent(i, "20")); // 5개 생성
        }

        // given
        String eventTypeCd = "20";
        String url = "http://localhost:" + port + "/api/main-events/operStat/" + eventTypeCd;

        // when
        MvcResult result = mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        String responseBody = result.getResponse().getContentAsString();

        // JSON 응답을 JsonNode로 파싱하여 eventId만 추출
        JsonNode rootNode = objectMapper.readTree(responseBody);
        List<String> actualEventIds = new ArrayList<>();

        JsonNode dataNode = rootNode.get("data");

        if (dataNode != null && dataNode.isArray()) {
            for (JsonNode node : dataNode) {
                System.out.println(node);
                if (node.has("operStatDt")) {
                    actualEventIds.add(node.get("operStatDt").asText());
                }
            }
        }
        System.out.println("Response Body (operStatDt 추출 완료): " + actualEventIds);

        assertThat(actualEventIds).hasSize(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate endDate1 = LocalDate.now().plusDays(1);
        LocalDate endDate2 = LocalDate.now().plusDays(2);
        LocalDate endDate3 = LocalDate.now().plusDays(3);
        LocalDate endDate4 = LocalDate.now().plusDays(4);
        LocalDate endDate5 = LocalDate.now().plusDays(5);

        assertThat(actualEventIds.get(0)).isEqualTo(endDate1.format(formatter));
        assertThat(actualEventIds.get(1)).isEqualTo(endDate2.format(formatter));
        assertThat(actualEventIds.get(2)).isEqualTo(endDate3.format(formatter));
        assertThat(actualEventIds.get(3)).isEqualTo(endDate4.format(formatter));
        assertThat(actualEventIds.get(4)).isEqualTo(endDate5.format(formatter));

        System.out.println("Events_오픈임박순_조회된다 테스트 성공");
    }


    @Test
    @WithMockUser(roles="USER")
    void Events_추천_조회된다() throws Exception {

        // given
        Set<Category> addCategories = Set.of(Category.PASH, Category.BEAU);
        Set<Category> updateCategories = Set.of(Category.IT, Category.SPORT);

        String addUrl = "http://localhost:" + port + "/api/user-categories";
        String getUrl = "http://localhost:" + port + "/api/user-categories";

        // when: 추가 요청
        mvc.perform(post(addUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCategories)))
                .andExpect(status().isOk());

        // then: 조회 요청 후 추가 확인
        MvcResult readyResult = mvc.perform(get(getUrl))
                .andExpect(status().isOk())
                .andReturn();

        // given
        String eventTypeCd = "10";
        String url = "http://localhost:" + port + "/api/main-events/rcmn/" + eventTypeCd;

        // when
        MvcResult result = mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        // ⭐ MvcResult.getResponse().getContentAsString() 대신 바이트 배열로 가져와 UTF-8로 명시적 디코딩
        String responseBody = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);

        // JSON 응답을 JsonNode로 파싱하여 eventNm만 추출
        JsonNode rootNode = objectMapper.readTree(responseBody);

        System.out.println("rootNode : " + rootNode);
        List<String> actualEventNms = new ArrayList<>();
        JsonNode dataNode2 = rootNode.get("categories").get("PASH");
        System.out.println("dataNode2 : " + dataNode2);
        List<String> nearEventIds = new ArrayList<>();
        System.out.println(dataNode2);
        if (dataNode2 != null && dataNode2.isArray()) {
            for (JsonNode node : dataNode2) {
                if (node.has("eventNm")) {
                    nearEventIds.add(node.get("eventNm").asText());
                }
            }
        }
        System.out.println("Response Body (eventNm 추출 완료): " + nearEventIds);

        assertThat(nearEventIds).hasSize(2);

        // ✨ 하드코딩된 예상 조회수 순서를 직접 비교
        // 예상되는 eventId 순서 (가장 큰 값부터 내림차순)
//        assertThat(actualEventNms.get(0)).isEqualTo("테스트 이벤트 4");
//        assertThat(actualEventNms.get(1)).isEqualTo("테스트 이벤트 3");
//        assertThat(actualEventNms.get(2)).isEqualTo("테스트 이벤트 2");
//        assertThat(actualEventNms.get(3)).isEqualTo("테스트 이벤트 1");
//        assertThat(actualEventNms.get(4)).isEqualTo("테스트 이벤트 0");

        System.out.println("Events_인기순으로_조회된다 테스트 성공");
    }

    @Test
    @WithMockUser(roles="USER")
    void Events_상세컨텐츠_조회된다() throws Exception {

        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        // given
        Object resultData = eventsRepository.getMainRankList("10").get(0);
        // Object[]로 형변환하여 데이터를 추출
        Object[] row = (Object[]) resultData;
        Long eventId = (Long) row[0];

        System.out.println("bf start ========= id >>" + eventId);
        String url = "http://localhost:" + port + "/api/events/cntn/" + eventId;

        MvcResult result = mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        String responseBody = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);

        // JSON 응답을 JsonNode로 파싱하여 eventNm만 추출
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode eventDetailsNode = rootNode.get("eventDetails");
        String eventNm = "";

        // eventDetailsNode가 null이 아니고, 'eventNm' 키를 포함하고 있는지 확인
        if (eventDetailsNode != null && eventDetailsNode.has("eventNm")) {
            eventNm = eventDetailsNode.get("eventNm").asText();
        }
        System.out.println("Response Body (eventNm 추출 완료): " + eventNm);

        // ✨ 하드코딩된 예상 조회수 순서를 직접 비교
        // 예상되는 eventId 순서 (가장 큰 값부터 내림차순)
        assertThat(eventNm.equals("테스트 이벤트 0"));

        JsonNode dataNode2 = rootNode.get("nearEvents");
        List<String> nearEventIds = new ArrayList<>();
        System.out.println(dataNode2);
        if (dataNode2 != null && dataNode2.isArray()) {
            for (JsonNode node : dataNode2) {
                System.out.println(node);
                if (node.has("eventNm")) {
                    nearEventIds.add(node.get("eventNm").asText());
                }
            }
        }
        System.out.println("nearEvents Response Body (eventName 추출 완료): " + nearEventIds);
        assertThat(nearEventIds).hasSize(4);
        assertThat(nearEventIds.get(0)).isEqualTo("테스트 이벤트 0");
        assertThat(nearEventIds.get(1)).isEqualTo("테스트 이벤트 1");
        assertThat(nearEventIds.get(2)).isEqualTo("테스트 이벤트 2");
        assertThat(nearEventIds.get(3)).isEqualTo("테스트 이벤트 3");

        System.out.println("Events_상세컨텐츠_조회된다 테스트 성공");
    }

}