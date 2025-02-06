package com.snowroad.config.auth;

import com.snowroad.common.util.JwtTokenProvider;
import com.snowroad.config.auth.dto.SessionUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String userId = authentication.getName();  // OAuth2 로그인한 사용자 ID
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("user");

        String accessToken = jwtTokenProvider.generateAccessToken(String.valueOf(sessionUser.getId()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(String.valueOf(sessionUser.getId()));

        // 쿠키에 JWT 저장
        addCookie(response, "access_token", accessToken, 30 * 60); // 30분
        addCookie(response, "refresh_token", refreshToken, 7 * 24 * 60 * 60); // 7일

        // 로그인 후 리다이렉트
        response.sendRedirect("/");
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);  // JavaScript에서 접근 불가능
        //cookie.setSecure(true);    // HTTPS에서만 전송
        cookie.setPath("/");       // 모든 경로에서 사용 가능
        cookie.setMaxAge(maxAge);  // 쿠키 유효시간 설정
        response.addCookie(cookie);
    }
}
