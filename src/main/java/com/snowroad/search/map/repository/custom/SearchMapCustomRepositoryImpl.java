package com.snowroad.search.map.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.search.map.dto.QSearchMapResponseDTO;
import com.snowroad.search.map.dto.SearchMapRequestDTO;
import com.snowroad.search.map.dto.SearchMapResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * Map 검색 커스텀 레포 구현체
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-28
 *
 */
@Repository
@RequiredArgsConstructor
public class SearchMapCustomRepositoryImpl implements SearchMapCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchMapResponseDTO> findLocationMapDataList(SearchMapRequestDTO requestDTO) {
        QSearchMapResponseDTO qSearchMapResponseDTO = QSearchMapResponseDTO.searchMapResponseDTO;
        BooleanBuilder builder = new BooleanBuilder();

        //지도 거리 계산
        if(requestDTO.getLatitude() != null && requestDTO.getLongitude() != null){
            BooleanExpression latCondition = qSearchMapResponseDTO.addrLttd.between(requestDTO.getMinLat(), requestDTO.getMaxLat());
            BooleanExpression lonCondition = qSearchMapResponseDTO.addrLotd.between(requestDTO.getMinLon(), requestDTO.getMaxLon());
            BooleanTemplate locationGroupedCondition = Expressions.booleanTemplate("(({0}) AND ({1}))", latCondition, lonCondition);
            builder.and(locationGroupedCondition);
        }

        //시작일자, 종료일자
        if (requestDTO.getOperStatDt() != null && requestDTO.getOperEndDt() != null) {
            BooleanExpression statCondition = qSearchMapResponseDTO.operStatDt.loe(requestDTO.getOperEndDt());
            BooleanExpression endCondition = qSearchMapResponseDTO.operEndDt.goe(requestDTO.getOperStatDt());
            BooleanTemplate dateGroupedCondition = Expressions.booleanTemplate("(({0}) AND ({1}))", statCondition, endCondition);
            builder.and(dateGroupedCondition);
        }

        //이벤트 구분 코드
        if (requestDTO.getEventTypeCd() != null) {
            builder.and(qSearchMapResponseDTO.evntTypeCd.eq(requestDTO.getEventTypeCd()));
        }

        return queryFactory
            .selectFrom(qSearchMapResponseDTO)
            .where(builder)
            .fetch();
    }
}
