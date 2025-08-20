package com.snowroad.config.auth;

import com.snowroad.common.util.JwtTokenProvider;
import com.snowroad.config.auth.dto.SessionUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
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
        addCookie(request, response, "access_token", accessToken, ACCESS_TOKEN_COOKIE_EXPIRY);
        addCookie(request, response, "refresh_token", refreshToken, REFRESH_TOKEN_COOKIE_EXPIRY);

        System.out.println(sessionUser.getJoinYn());
        String targetUrl = redirectUrl;
        if ("N".equals(sessionUser.getJoinYn())) {
            targetUrl = redirectUrl + "/register";
        }
        // 로그인 후 리다이렉트
        response.sendRedirect(targetUrl);
    }

    private void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge) {
        String serverName = request.getServerName();  // ex: localhost, api.noongil.org

        boolean isLocal = serverName.equals("localhost") || serverName.equals("127.0.0.1");

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .maxAge(maxAge);

        if (isLocal) {
            // 로컬 개발 환경
            cookieBuilder.sameSite("Lax");      // 로컬에서는 Lax로도 충분
            cookieBuilder.secure(false);        // HTTPS 사용 안 하므로 false
        } else {
            // 운영 환경
            cookieBuilder.sameSite("None");     // cross-site 쿠키 허용
            cookieBuilder.secure(true);         // HTTPS 필수
            cookieBuilder.domain(".noongil.org");
        }

        ResponseCookie cookie = cookieBuilder.build();
             
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
