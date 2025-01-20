package com.snowroad.search.MorphemeAnalyzer.service;

import com.snowroad.search.MorphemeAnalyzer.interfaces.MorphemeAnalyzerInterface;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

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
public class KomoranMorphemeAnalyzerService implements MorphemeAnalyzerInterface {

    public List<String> morphemeAnalyzer (String text) {
        List<String> morphemeAnalyzerTextList = new LinkedList<>();

        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        KomoranResult analyzeResultList = komoran.analyze(text);
        log.info(analyzeResultList.getPlainText());

        List<Token> tokenList = analyzeResultList.getTokenList();
        for (Token token : tokenList) {
            log.info("({} , {}) {}/{}", token.getBeginIndex(), token.getEndIndex(), token.getMorph(), token.getPos());
        }
        return morphemeAnalyzerTextList;
    }

}
