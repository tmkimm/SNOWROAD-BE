package com.snowroad.MorphemeAnalyzer.komoran.service;

import com.snowroad.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.MorphemeAnalyzer.komoran.enums.KomoranPOS;
import com.snowroad.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
@RequiredArgsConstructor
public class KomoranAnalyzerService implements KomoranAnalyzerInterface {

    private final Komoran komoran;

    public Map<String, List<KomoranDTO>> komoranAnalyzerMap(String keyword) {
        KomoranResult analyzeResultList = komoran.analyze(keyword);
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

            //따옴표, 괄호표, 줄표를 제외 합니다.
            if(Objects.equals(token.getPos(), "SS")) continue;

            //기타기호(논리수학기호, 화폐기호)를 제외 합니다.
            if(Objects.equals(token.getPos(), "SW")) continue;

            komoranDTOList.add(KomoranDTO.builder()
                    .token(token.getMorph())
                    .posCode(token.getPos())
                    .posName(KomoranPOS.getPosNameByKey(token.getPos()))
                    .build()
            );
        }
        return komoranDTOList.stream().collect(Collectors.groupingBy(KomoranDTO::getPosCode));
    }

}
