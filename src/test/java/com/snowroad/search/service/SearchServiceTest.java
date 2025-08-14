package com.snowroad.search.service;

import com.snowroad.MorphemeAnalyzer.event.domain.EventNounMran;
import com.snowroad.MorphemeAnalyzer.event.repository.EventNounMranRepository;
import com.snowroad.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import com.snowroad.entity.Events;
import com.snowroad.geodata.interfaces.GeoDataInterface;
import com.snowroad.search.dto.SearchPagedResponse;
import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.dto.SearchResponseDTO;
import com.snowroad.search.repository.SearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private KomoranAnalyzerInterface komoranAnalyzerInterface;

    @Mock
    private EventNounMranRepository eventNounMranRepository;

    @Mock
    private GeoDataInterface geoDataInterface;

    @InjectMocks
    private SearchService searchService;

    private SearchRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        requestDTO = new SearchRequestDTO();
        requestDTO.setKeyword("음악");
        requestDTO.setPage(0);
        requestDTO.setEventTypeCd("10");
        requestDTO.setLatitude(37.55);
        requestDTO.setLongitude(127.00);
    }

    @Test
    @DisplayName("검색 키워드 + 위치 기반 이벤트 검색")
    void testGetEventsWithKeywordAndLocation() {
        // given
        KomoranDTO token = new KomoranDTO("음악", "NNG", "명사");
        when(komoranAnalyzerInterface.komoranAnalyzerMap("음악"))
                .thenReturn(Map.of("tokens", List.of(token)));

        EventNounMran mran = new EventNounMran();
        mran.setEvntId(1L);
        when(eventNounMranRepository.findByMranCntn("음악"))
                .thenReturn(List.of(mran));

        Events event = Events.builder()
                .eventId(1L)
                .build();
        when(geoDataInterface.getGeoDataByLocation(37.55, 127.00))
                .thenReturn(List.of(event));

        SearchResponseDTO dto = new SearchResponseDTO(); // 필요 시 필드 추가
        Page<SearchResponseDTO> resultPage = new PageImpl<>(List.of(dto));
        when(searchRepository.findSearchEventDataList(any()))
                .thenReturn(resultPage);

        // when
        SearchPagedResponse result = searchService.getEvents(requestDTO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEvents()).hasSize(1);
        verify(searchRepository, times(1)).findSearchEventDataList(any());
    }

    @Test
    @DisplayName("위치 기반 이벤트 거리 필터링 정확성 확인")
    void testLocationDistanceFilteringApplied() {
        // given
        // 키워드 없음 → keyword 검색 생략
        requestDTO.setKeyword(null);

        // 위치 정보 설정
        requestDTO.setLatitude(37.55);
        requestDTO.setLongitude(127.00);

        // 위치 기반 반환값 mock (eventId 100L 반환)
        Events event = Events.builder()
                .eventId(100L)
                .build();
        when(geoDataInterface.getGeoDataByLocation(37.55, 127.00))
                .thenReturn(List.of(event));

        // repository는 해당 eventId만 반환해야 함
        SearchResponseDTO dto = new SearchResponseDTO(
                100L, "거리 이벤트", "내용", "서울시", "도로명", "지번",
                "20250101", "20250110", "10:00~20:00",
                "CTGY01", "10", 37.55, 127.00, "11680101", "http://img.url", 111L, 50, "Y"
        );
        dto.setDistanceKm(0.5);
        dto.setDisplayDistance("0.5km");

        Page<SearchResponseDTO> resultPage = new PageImpl<>(List.of(dto));
        when(searchRepository.findSearchEventDataList(any()))
                .thenReturn(resultPage);

        // when
        SearchPagedResponse result = searchService.getEvents(requestDTO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEvents()).hasSize(1);
        assertThat(result.getEvents().get(0).getEventId()).isEqualTo(100L);

        verify(geoDataInterface).getGeoDataByLocation(37.55, 127.00);
        verify(searchRepository, times(1)).findSearchEventDataList(any());
    }

    @Test
    @DisplayName("키워드만 있을 때 - 키워드 기반 검색만 수행")
    void testGetEventsWithOnlyKeyword() {
        // given
        requestDTO.setLatitude(null);
        requestDTO.setLongitude(null);

        KomoranDTO token = new KomoranDTO("음악", "NNG", "명사");
        when(komoranAnalyzerInterface.komoranAnalyzerMap("음악"))
                .thenReturn(Map.of("tokens", List.of(token)));

        EventNounMran mran = new EventNounMran();
        mran.setEvntId(1L);
        when(eventNounMranRepository.findByMranCntn("음악"))
                .thenReturn(List.of(mran));

        SearchResponseDTO dto = mock(SearchResponseDTO.class);
        Page<SearchResponseDTO> resultPage = new PageImpl<>(List.of(dto));
        when(searchRepository.findSearchEventDataList(any()))
                .thenReturn(resultPage);

        // when
        SearchPagedResponse result = searchService.getEvents(requestDTO);

        // then
        assertThat(result.getEvents()).hasSize(1);
        verify(geoDataInterface, never()).getGeoDataByLocation(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("위치만 있을 때 - 거리 기반 검색만 수행")
    void testGetEventsWithOnlyLocation() {
        // given
        requestDTO.setKeyword(null);

        Events event = Events.builder().eventId(2L).build();
        when(geoDataInterface.getGeoDataByLocation(37.55, 127.00))
                .thenReturn(List.of(event));

        SearchResponseDTO dto = mock(SearchResponseDTO.class);
        Page<SearchResponseDTO> resultPage = new PageImpl<>(List.of(dto));
        when(searchRepository.findSearchEventDataList(any()))
                .thenReturn(resultPage);

        // when
        SearchPagedResponse result = searchService.getEvents(requestDTO);

        // then
        assertThat(result.getEvents()).hasSize(1);
        verify(komoranAnalyzerInterface, never()).komoranAnalyzerMap(anyString());
    }

    @Test
    @DisplayName("키워드와 위치 둘 다 없을 때 - 전체 검색")
    void testGetEventsWithoutKeywordAndLocation() {
        // given
        requestDTO.setKeyword(null);
        requestDTO.setLatitude(null);
        requestDTO.setLongitude(null);

        SearchResponseDTO dto = mock(SearchResponseDTO.class);
        Page<SearchResponseDTO> resultPage = new PageImpl<>(List.of(dto));
        when(searchRepository.findSearchEventDataList(any()))
                .thenReturn(resultPage);

        // when
        SearchPagedResponse result = searchService.getEvents(requestDTO);

        // then
        assertThat(result.getEvents()).hasSize(1);
        verify(komoranAnalyzerInterface, never()).komoranAnalyzerMap(anyString());
        verify(geoDataInterface, never()).getGeoDataByLocation(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("키워드와 위치 모두 있는 경우 - 교집합 필터링")
    void testGetEventsWithIntersectionFiltering() {
        // given
        KomoranDTO token = new KomoranDTO("음악", "NNG", "명사");
        when(komoranAnalyzerInterface.komoranAnalyzerMap("음악"))
                .thenReturn(Map.of("tokens", List.of(token)));

        EventNounMran mran = new EventNounMran();
        mran.setEvntId(1L);
        when(eventNounMranRepository.findByMranCntn("음악"))
                .thenReturn(List.of(mran));

        Events event = Events.builder().eventId(1L).build();
        when(geoDataInterface.getGeoDataByLocation(37.55, 127.00))
                .thenReturn(List.of(event));

        SearchResponseDTO dto = mock(SearchResponseDTO.class);
        Page<SearchResponseDTO> resultPage = new PageImpl<>(List.of(dto));
        when(searchRepository.findSearchEventDataList(any()))
                .thenReturn(resultPage);

        // when
        SearchPagedResponse result = searchService.getEvents(requestDTO);

        // then
        assertThat(result.getEvents()).hasSize(1);
        verify(searchRepository).findSearchEventDataList(argThat(req -> req.getEventIds().contains(1L)));
    }

    @Test
    @DisplayName("정렬 기준이 없는 경우 기본값 설정 확인")
    void testGetEventsDefaultSortType() {
        // given
        requestDTO.setSortType(null);

        when(searchRepository.findSearchEventDataList(any()))
                .thenReturn(new PageImpl<>(List.of(mock(SearchResponseDTO.class))));

        // when
        searchService.getEvents(requestDTO);

        // then
        assertThat(requestDTO.getSortType()).isEqualTo("최신순");
    }


}
