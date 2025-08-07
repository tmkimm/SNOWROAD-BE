package com.snowroad.search.service;

import com.snowroad.search.dto.PopularSearch;
import com.snowroad.search.dto.PopularSearchResponse;
import com.snowroad.search.interfaces.PopularSearchInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopularSearchService implements PopularSearchInterface {

    @Override
    public PopularSearchResponse getPopularSearch() {
        List<PopularSearch> list = List.of(
                create(1L, "Enhypen 팝업스토어", "1"),
                create(2L, "Monsta X 굿즈 팝업", "2"),
                create(3L, "BT21 명동 팝업", "3"),
                create(4L, "ITZY 더현대서울 팝업", "4"),
                create(5L, "용산 Ipark몰 팝업 스토어", "5"),
                create(6L, "현대리바트 성수 전시", "6"),
                create(7L, "Dr.G 이영지 팝업", "7"),
                create(8L, "Tokyo Revengers 전시 홍대", "8"),
                create(9L, "Gentle Monster Bratz 팝업", "9"),
                create(10L, "Valentino 가을 팝업", "10")
        );
        return new PopularSearchResponse(list);
    }

    private PopularSearch create(Long id, String content, String rank) {
        PopularSearch p = new PopularSearch();
        p.setPplrSrchId(id);
        p.setPplrSrchCntn(content);
        p.setPplrSrchRank(rank);
        return p;
    }
}
