package com.snowroad.config.auth;

import com.snowroad.common.util.JwtTokenProvider;
import com.snowroad.config.auth.dto.SessionUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${access.token.cookie.expiry}")
    private int ACCESS_TOKEN_COOKIE_EXPIRY;

    @Value("${refresh.token.cookie.expiry}")
    private int REFRESH_TOKEN_COOKIE_EXPIRY;

    @Value("${app.redirect-url:http://ec2-13-125-216-97.ap-northeast-2.compute.amazonaws.com}") // 기본값 설정
    private String redirectUrl;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("user");

        String accessToken = jwtTokenProvider.generateAccessToken(String.valueOf(sessionUser.getId()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(String.valueOf(sessionUser.getId()));

        // 쿠키에 JWT 저장
        addCookie(response, "access_token", accessToken, ACCESS_TOKEN_COOKIE_EXPIRY);
        addCookie(response, "refresh_token", refreshToken, REFRESH_TOKEN_COOKIE_EXPIRY);
        // 로그인 후 리다이렉트
        response.sendRedirect(redirectUrl);
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);  // JavaScript에서 접근 불가능
        cookie.setPath("/");       // 모든 경로에서 사용 가능
        cookie.setMaxAge(maxAge);  // 쿠키 유효시간 설정
        response.addCookie(cookie);
    }
}
