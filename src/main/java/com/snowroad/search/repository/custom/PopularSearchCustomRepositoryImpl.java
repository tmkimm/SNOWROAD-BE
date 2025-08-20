package com.snowroad.search.repository.custom;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.search.dto.PopularSearch;
import com.snowroad.search.dto.QPopularSearch;
import com.snowroad.search.entity.QSearchLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PopularSearchCustomRepositoryImpl implements  PopularSearchCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PopularSearch> findTopPopularKeywords(int limit, Duration duration) {
        LocalDateTime from = LocalDateTime.now().minus(duration);
        QSearchLog searchLog = new QSearchLog("searchLog");

        // 인기검색어 쿼리 구성
        JPAQuery<PopularSearch> query = queryFactory
                .select(new QPopularSearch(
                        // pplrSrchId: 실시간 계산이므로 null 반환
                        Expressions.nullExpression(Long.class),
                        // pplrSrchCntn: 검색어 자체
                        searchLog.srchCntn,
                        // pplrSrchRank: 순위 (1위부터 순서대로) – 문자형으로 캐스팅
                        Expressions.stringTemplate(
                                "CAST(ROW_NUMBER() OVER (ORDER BY COUNT(*) DESC) AS CHAR)"
                        )
                ))
                .from(searchLog)
                .where(
                        searchLog.dataDeltDttm.isNull(),
                        searchLog.srchCntn.isNotNull(),
                        searchLog.srchCntn.length().goe(2),
                        searchLog.dataCrtnDttm.goe(from)
                )
                .groupBy(searchLog.srchCntn)
                .orderBy(searchLog.srchCntn.count().desc())
                .limit(limit);

        return query.fetch();
    }
}
