package com.snowroad.common.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        String serverName = request.getServerName();  // ex: localhost, api.noongil.org
        boolean isLocal = serverName.equals("localhost") || serverName.equals("127.0.0.1");

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0); // 즉시 만료

        if (isLocal) {
            cookieBuilder.sameSite("Lax");
            cookieBuilder.secure(false);
        } else {
            cookieBuilder.sameSite("None");
            cookieBuilder.secure(true);
            cookieBuilder.domain(".noongil.org");  // ❗️쿠키 생성할 때와 동일한 도메인 지정
        }

        ResponseCookie cookie = cookieBuilder.build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // access/refresh 토큰 삭제
    public void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response, "access_token");
        deleteCookie(request, response, "refresh_token");
    }

    // admin access/refresh 토큰 삭제
    public void clearCookiesAdmin(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response, "access_token_admin");
        deleteCookie(request, response, "refresh_token_admin");
    }
}