package com.snowroad.config.auth;


import com.snowroad.common.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@EnableWebSecurity  // spring security 설정 활성화
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2AuthenticationSuccessHandler successHandler;
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
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
                        .anyRequest().permitAll())  // 나머지 url은 모두 인증된 사용자만 허용
                .logout(logout -> logout
                        .logoutSuccessUrl("/"))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(successHandler)
                )
                .addFilterBefore(new JwtCookieAuthenticationFilter(jwtTokenProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
