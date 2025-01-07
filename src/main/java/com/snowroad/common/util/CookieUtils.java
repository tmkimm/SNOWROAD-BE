package com.snowroad.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {
    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final int ACCESS_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24;  // 1일

    private static final int REFRESH_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24 * 7;  // 30일
    public static void addAccessTokenToCookies(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);    // 클라이언트 측 JavaScript에서 쿠키에 접근하지 못하도록 설정
        cookie.setSecure(true);      // HTTPS를 사용하는 경우에만 전송
        cookie.setPath("/");        // 쿠키의 경로 설정
        cookie.setMaxAge(ACCESS_TOKEN_COOKIE_MAX_AGE);     // 쿠키의 만료 시간 설정 (1시간)

        // 응답에 쿠키 추가
        response.addCookie(cookie);
    }
    public static void addRefreshTokenToCookies(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);    // 클라이언트 측 JavaScript에서 쿠키에 접근하지 못하도록 설정
        cookie.setSecure(true);      // HTTPS를 사용하는 경우에만 전송
        cookie.setPath("/");        // 쿠키의 경로 설정
        cookie.setMaxAge(REFRESH_TOKEN_COOKIE_MAX_AGE);     // 쿠키의 만료 시간 설정 (1시간)

        // 응답에 쿠키 추가
        response.addCookie(cookie);
    }

}
