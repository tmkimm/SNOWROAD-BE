package com.snowroad.event.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    // 팝업 카테고리
    PASH("PASH", "패션"),          // FASHION
    CHAR("CHAR", "캐릭터"),   // CHARACTER
    FANDB("FANDB", "식음료(F&B)"), // FANDB
    BEAU("BEAU", "뷰티"),   // BEAUTY
    IT("IT", "IT"),
    LIFT("LIFT", "라이프스타일"),
    ENTER("ENTER", "엔터"),
    SPORT("SPORT", "스포츠/건강"),

    // 전시 카테고리
    PHOT("PHOT", "사진전"),             // 사진전 PHOTOGRAPHY
    PAINT("PAINT", "그림전"),               // 회화 및 전통적인 그림 PAINTING
    MEDIA("MEDIA", "미디어 아트"),         // 디지털 기반 전시 MEDIA_ART
    SCULP("SCULP", "조각/설치미술"),      // 설치미술 SCULPTURE
    DESIGN("DESIGN", "디자인"),                // 디자인 아트
    EXPER("EXPER", "체험형 전시"),        // 체험형 전시 EXPERIENCE
    CHILD("CHILD", "어린이"),               // 어린이를 위한 전시 CHILDREN
    COMIC("COMIC", "만화");                  // 만화 관련 전시 COMICS
    
    private final String key;
    private final String title;
}