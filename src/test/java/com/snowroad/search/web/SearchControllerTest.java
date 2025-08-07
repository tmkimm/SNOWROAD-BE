package com.snowroad.search.web;

import com.snowroad.search.dto.PopularSearchResponse;
import com.snowroad.search.dto.SearchPagedResponse;
import com.snowroad.search.dto.SearchRequestDTO;

import com.snowroad.search.interfaces.PopularSearchInterface;
import com.snowroad.search.interfaces.SearchInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchInterface searchInterface;

    @MockBean
    private PopularSearchInterface popularSearchInterface;

    @Test
    @DisplayName("이벤트 검색 성공")
    void testGetEvents_success() throws Exception {
        // given
        SearchPagedResponse mockResponse = new SearchPagedResponse();
        Mockito.when(searchInterface.getEvents(any(SearchRequestDTO.class)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/search/events")
                        .param("keyword", "축제")
                        .param("sortType", "")
                        .param("page", "0")
                        .param("latitude", "37.527097226615")
                        .param("longitude", "126.92730122817")
                        .param("operStatDt", "20250101")
                        .param("operEndDt", "20250731")
                        .param("eventTypeCd", "10")
                        .param("categories", "BEAU")
                        .param("categories", "CHAR")
                        .param("regionGroups", "10")
                        .param("regionGroups", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인기 검색어 조회 성공")
    void testGetPopularSearch_success() throws Exception {
        // given
        PopularSearchResponse mockResponse = new PopularSearchResponse();
        Mockito.when(popularSearchInterface.getPopularSearch()).thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/search/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}