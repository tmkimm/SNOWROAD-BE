package com.snowroad.search;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.snowroad.entity.Events;
import com.snowroad.search.dto.SearchPagedResponse;
import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.repository.SearchRepository;
import com.snowroad.search.service.SearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    public void test_eventSearch() {
        // given
        SearchRequestDTO req = new SearchRequestDTO();
        req.setPage(0);

        Sort expectedSort = Sort.by(Sort.Direction.DESC, "operStatDt");
        Pageable expectedPageable = PageRequest.of(0, 12, expectedSort);

        List<Events> dummyList = List.of(new Events(), new Events());
        Page<Events> dummyPage = new PageImpl<>(dummyList, expectedPageable, 2L);

        given(searchRepository.findSearchEventDataList(eq(expectedPageable), eq(req)))
                .willReturn(dummyPage);

        // when
        SearchPagedResponse resp = searchService.getEvents(req);

        // then
        assertThat(resp.getEvents()).hasSize(2);
        assertThat(resp.getTotalCount()).isEqualTo(2);
        assertThat(resp.getTotalPageCount()).isEqualTo(1);
        verify(searchRepository).findSearchEventDataList(expectedPageable, req);
    }

}
