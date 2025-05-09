package com.snowroad.config.auth;


import com.snowroad.common.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@EnableWebSecurity  // spring security 설정 활성화
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2AuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // h2-console을 위해 옵션을 disable
                .headers((headersConfigurer) -> {
                    headersConfigurer.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable());
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile", "/swagger-ui/*")
                        .permitAll()// 모두 허용
//                        .requestMatchers("/api/**").hasRole(Role.USER.name())    // user 권한을 가진 사람만 허용
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // ADMIN 권한만 허용
                        .anyRequest().permitAll()
                )  // 나머지 url은 모두 인증된 사용자만 허용
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("access_token", "refresh_token","access_token_admin", "refresh_token_admin")
                        .clearAuthentication(true))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(successHandler)
                )
                .addFilterBefore(new JwtCookieAuthenticationFilter(jwtTokenProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
