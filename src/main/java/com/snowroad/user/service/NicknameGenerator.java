package com.snowroad.user.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class NicknameGenerator {

    private static final List<String> ADJECTIVES = List.of(
            "행복한", "귀여운", "멋진", "용감한", "똑똑한", "재미있는", "강력한", "신비로운", "수줍은", "활발한",
            "차분한", "씩씩한", "느긋한", "상냥한", "다정한", "엉뚱한", "기발한", "유쾌한", "소심한", "우아한",
            "엉뚱한", "씩씩한", "장난꾸러기", "대담한", "날렵한", "화려한", "자유로운", "반짝이는", "신나는", "온순한"
    );

    private static final List<String> NOUNS = List.of(
            "고래", "호랑이", "사자", "햄스터", "용", "부엉이", "토끼", "늑대", "펭귄", "대마왕",
            "여우", "두더지", "수달", "고양이", "강아지", "얼룩말", "다람쥐", "판다", "코끼리", "참새",
            "악어", "곰", "오리", "타조", "두루미", "청설모", "카멜레온", "알파카", "미어캣", "고슴도치"
    );

    private final Random random = new Random();

    public String generate() {
        String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(random.nextInt(NOUNS.size()));
        return adjective + " " + noun;
    }
}
