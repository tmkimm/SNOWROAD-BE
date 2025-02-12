package com.snowroad.search.MorphemeAnalyzer.komoran.service;

import com.snowroad.search.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.search.MorphemeAnalyzer.komoran.enums.KomoranPOS;
import com.snowroad.search.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Komoran 형태소 분석기
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-01-15
 *
 */
@Slf4j
@Service
public class KomoranAnalyzerService implements KomoranAnalyzerInterface {

    private final Komoran komoran;

    public KomoranAnalyzerService() {
        this.komoran = new Komoran(DEFAULT_MODEL.FULL);
    }

    public Map<String, List<KomoranDTO>> komoranAnalyzerMap(String text) {
        KomoranResult analyzeResultList = komoran.analyze(text);
        log.info(analyzeResultList.getPlainText());

        List<KomoranDTO> komoranDTOList = new LinkedList<>();
        List<Token> tokenList = analyzeResultList.getTokenList();
        for (Token token : tokenList) {
            log.debug("({} , {}) {}/{}_{}"
                    , token.getBeginIndex()
                    , token.getEndIndex()
                    , token.getMorph()
                    , token.getPos()
                    , KomoranPOS.getPosNameByKey(token.getPos())
            );
            komoranDTOList.add(KomoranDTO.builder()
                    .token(token.getMorph())
                    .posCode(token.getPos())
                    .posName(KomoranPOS.getPosNameByKey(token.getPos()))
                    .build()
            );
        }
        Map<String, List<KomoranDTO>> groupedResults = komoranDTOList.stream()
                .collect(Collectors.groupingBy(KomoranDTO::getPosCode));
        for(List<KomoranDTO> resultList : groupedResults.values()){
            for (KomoranDTO komoranDTO : resultList) {
                log.info("Token :: ' {} '  Pos [ code : {} | name : {} ]" , komoranDTO.getToken(), komoranDTO.getPosCode(), komoranDTO.getPosName());
            }
        }
        return groupedResults;
    }

}
