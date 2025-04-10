package com.snowroad.config.auth;

import com.snowroad.common.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

    private static final int ACCESS_TOKEN_EXPIRATION_SECONDS = 30 * 60; // 30분

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtCookieAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        TokenNames tokenNames = resolveTokenNames(request);
        String accessToken = extractValidAccessToken(request, tokenNames.accessToken);

        if (accessToken == null) {
            String refreshToken = extractValidRefreshToken(request, tokenNames.refreshToken);

            if (refreshToken != null) {
                accessToken = refreshAndAddNewAccessToken(refreshToken, response, tokenNames.accessToken);
            }
        }

        if (accessToken != null) {
            authenticateUserFromToken(accessToken);
        }

        filterChain.doFilter(request, response);
    }

    private TokenNames resolveTokenNames(HttpServletRequest request) {
        boolean isAdmin = request.getRequestURI().startsWith("/api/admin");
        return isAdmin
                ? new TokenNames("access_token_admin", "refresh_token_admin")
                : new TokenNames("access_token", "refresh_token");
    }

    private String extractValidAccessToken(HttpServletRequest request, String cookieName) {
        String token = getTokenFromCookie(request, cookieName);
        return (token != null && jwtTokenProvider.validateAccessToken(token)) ? token : null;
    }
    private String extractValidRefreshToken(HttpServletRequest request, String cookieName) {
        String token = getTokenFromCookie(request, cookieName);
        return (token != null && jwtTokenProvider.validateRefreshToken(token)) ? token : null;
    }

    private String refreshAndAddNewAccessToken(String refreshToken, HttpServletResponse response, String accessTokenName) {
        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        addCookie(response, accessTokenName, newAccessToken, ACCESS_TOKEN_EXPIRATION_SECONDS);
        return newAccessToken;
    }

    private void authenticateUserFromToken(String token) {
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserDetails userDetails = loadUserIfExists(userId);

        if (userDetails != null) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private UserDetails loadUserIfExists(String userId) {
        try {
            return userDetailsService.loadUserByUsername(userId);
        } catch (UsernameNotFoundException e) {
            // 로그인하지 않은 사용자 요청일 수 있으므로 무시
            return null;
        }
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

    // 내부 클래스: 쿠키명 쌍을 깔끔하게 전달
    private static class TokenNames {
        final String accessToken;
        final String refreshToken;

        TokenNames(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}