package com.snowroad.config.auth;

import com.snowroad.common.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtCookieAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = getTokenFromCookie(request, "access_token");

        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            // Access Token이 없거나 유효하지 않다면 Refresh Token 확인
            String refreshToken = getTokenFromCookie(request, "refresh_token");

            if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                // Refresh Token이 유효하면 새 Access Token 발급
                String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
                String newAccessToken = jwtTokenProvider.generateAccessToken(userId);

                // 새 Access Token을 쿠키에 저장
                addCookie(response, "access_token", newAccessToken, 30 * 60); // 30분 유효

                accessToken = newAccessToken; // 새 Access Token으로 인증 수행
            }
        }

        // 최종적으로 Access Token이 유효하면 인증 처리
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            String userId = jwtTokenProvider.getUserIdFromToken(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

//            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookie(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        return (cookie != null) ? cookie.getValue() : null;
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
