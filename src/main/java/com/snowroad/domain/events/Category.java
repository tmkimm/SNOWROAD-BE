package com.snowroad.domain.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    // 팝업 카테고리
    FASHION("PASH", "패션"),  
    FOOD("FOOD", "음식"),      
    BEAUTY("BEAU", "뷰티"),    
    ENTERTAINMENT("ENTE", "엔터테이먼트"),  
    EXPERIENCE("EXPE", "체험형"),   
    CHARACTER("CHAR", "캐릭터"),     
    MISC("MISC", "잡화"),        

    // 전시 카테고리
    ART("ART", "미술"),           
    PHOTOGRAPHY("PHOT", "사진전"),   
    EXHIBITION("EXHI", "체험형 전시");     
    
    private final String key;
    private final String title;
}
