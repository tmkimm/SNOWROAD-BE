package com.snowroad.MorphemeAnalyzer.komoran.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * KomoranPOS
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-08
 *
 */
@Getter
@RequiredArgsConstructor
public enum KomoranPOS {

    // Nominal (체언)
    COMMON_NOUN(KomoranCategory.NOMINAL, "NNG", "일반명사"),
    PROPER_NOUN(KomoranCategory.NOMINAL, "NNP", "고유명사"),
    DEPENDENT_NOUN(KomoranCategory.NOMINAL, "NNB", "의존명사"),
    PRONOUN(KomoranCategory.NOMINAL, "NP", "대명사"),
    NUMERAL(KomoranCategory.NOMINAL, "NR", "수사"),

    // Verbal (용언)
    VERB(KomoranCategory.VERBAL, "VV", "동사"),
    ADJECTIVE(KomoranCategory.VERBAL, "VA", "형용사"),
    AUXILIARY_VERB(KomoranCategory.VERBAL, "VX", "보조용언"),
    AFFIRMATIVE_COPULA(KomoranCategory.VERBAL, "VCP", "긍정지정사"),
    NEGATIVE_COPULA(KomoranCategory.VERBAL, "VCN", "부정지정사"),

    // Modifier (수식언)
    DETERMINER(KomoranCategory.MODIFIER, "MM", "관형사"),
    GENERAL_ADVERB(KomoranCategory.MODIFIER, "MAG", "일반부사"),
    CONJUNCTIVE_ADVERB(KomoranCategory.MODIFIER, "MAJ", "접속부사"),

    // Independent (독립언)
    INTERJECTION(KomoranCategory.INDEPENDENT, "IC", "감탄사"),

    // Relational (관계언)
    NOMINATIVE_POSTPOSITION(KomoranCategory.RELATIONAL, "JKS", "주격조사"),
    PREDICATIVE_POSTPOSITION(KomoranCategory.RELATIONAL, "JKC", "보격조사"),
    ATTRIBUTIVE_POSTPOSITION(KomoranCategory.RELATIONAL, "JKG", "관형격조사"),
    OBJECTIVE_POSTPOSITION(KomoranCategory.RELATIONAL, "JKO", "목적격조사"),
    ADVERBIAL_POSTPOSITION(KomoranCategory.RELATIONAL, "JKB", "부사격조사"),
    VOCATIVE_POSTPOSITION(KomoranCategory.RELATIONAL, "JKV", "호격조사"),
    QUOTATIVE_POSTPOSITION(KomoranCategory.RELATIONAL, "JKQ", "인용격조사"),
    AUXILIARY_PARTICLE(KomoranCategory.RELATIONAL, "JX", "보조사"),
    CONJUNCTIVE_PARTICLE(KomoranCategory.RELATIONAL, "JC", "접속조사"),

    // Dependent (의존형태)
    PRE_FINAL_ENDING(KomoranCategory.DEPENDENT, "EP", "선어말어미"),
    FINAL_ENDING(KomoranCategory.DEPENDENT, "EF", "종결어미"),
    CONNECTIVE_ENDING(KomoranCategory.DEPENDENT, "EC", "연결어미"),
    NOMINALIZING_ENDING(KomoranCategory.DEPENDENT, "ETN", "명사형전성어미"),
    ATTRIBUTIVE_ENDING(KomoranCategory.DEPENDENT, "ETM", "관형형전성어미"),
    PREFIX(KomoranCategory.DEPENDENT, "XPN", "체언접두사"),
    NOUN_DERIVATIONAL_SUFFIX(KomoranCategory.DEPENDENT, "XSN", "명사파생접미사"),
    VERB_DERIVATIONAL_SUFFIX(KomoranCategory.DEPENDENT, "XSV", "동사파생접미사"),
    ADJECTIVE_DERIVATIONAL_SUFFIX(KomoranCategory.DEPENDENT, "XSA", "형용사파생접미사"),
    ROOT(KomoranCategory.DEPENDENT, "XR", "어근"),

    // Symbol (기호)
    SENTENCE_TERMINAL(KomoranCategory.SYMBOL, "SF", "마침표, 물음표, 느낌표"),
    PUNCTUATION_SEPARATOR(KomoranCategory.SYMBOL, "SP", "쉼표, 가운뎃점, 콜론, 빗금"),
    QUOTATION_BRACKET(KomoranCategory.SYMBOL, "SS", "따옴표, 괄호표, 줄표"),
    ELLIPSIS(KomoranCategory.SYMBOL, "SE", "줄임표"),
    ATTACHMENT_MARK(KomoranCategory.SYMBOL, "SO", "붙임표(물결, 숨김, 빠짐)"),
    FOREIGN_WORD(KomoranCategory.SYMBOL, "SL", "외국어"),
    HANJA(KomoranCategory.SYMBOL, "SH", "한자"),
    OTHER_SYMBOL(KomoranCategory.SYMBOL, "SW", "기타기호(논리수학기호, 화폐기호)"),
    NOMINAL_GUESS_CATEGORY(KomoranCategory.SYMBOL, "NF", "명사추정범주"),
    VERBAL_GUESS_CATEGORY(KomoranCategory.SYMBOL, "NV", "용언추정범주"),
    NUMERIC(KomoranCategory.SYMBOL, "SN", "숫자"),
    UNANALYZED_CATEGORY(KomoranCategory.SYMBOL, "NA", "분석불능범주");

    private final KomoranCategory groupKey;
    private final String posKey;
    private final String posName;

    private static final Map<String, KomoranPOS> LOOKUP;

    static {
        Map<String, KomoranPOS> lookup = new HashMap<>();
        for (KomoranPOS pos : KomoranPOS.values()) {
            lookup.put(pos.getPosKey(), pos);
        }
        LOOKUP = Collections.unmodifiableMap(lookup);
    }

    /**
     *
     * 품사Key 를 이용 해서 품사 명을 가져 옵니다.
     *
     * @param posKey 품사 Key 값
     * @return 전달 받은 품사Key(posKey)에 해당 하는 품사명(posName)을 반환 한다 / 없는 경우 null 을 반환 한다
     */
    public static String getPosNameByKey(String posKey) {
        KomoranPOS pos = LOOKUP.get(posKey);
        return pos != null ? pos.getPosName() : null;
    }
}