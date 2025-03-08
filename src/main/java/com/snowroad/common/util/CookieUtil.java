package com.snowroad.common.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    // 쿠키 삭제 메서드
    public void clearCookies(HttpServletResponse response) {
        // access_token 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/"); // 전체 경로에서 접근 가능하도록 설정
        accessTokenCookie.setMaxAge(0); // 쿠키 만료 시간 0으로 설정 (즉시 삭제)
        response.addCookie(accessTokenCookie);

        // refresh_token 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/"); // 전체 경로에서 접근 가능하도록 설정
        refreshTokenCookie.setMaxAge(0); // 쿠키 만료 시간 0으로 설정 (즉시 삭제)
        response.addCookie(refreshTokenCookie);
    }
}