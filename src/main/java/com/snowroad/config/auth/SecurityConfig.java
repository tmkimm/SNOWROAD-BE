package com.snowroad.config.auth;


import com.snowroad.domain.user.Role;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // spring security 설정 활성화
public class SecurityConfig {
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (선택 사항)
                .headers((headersConfigurer -> {
                    headersConfigurer.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable());
                })) // h2-console 화면을 위해 해당 옵션 disable
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile")
                        .permitAll()// 모두 허용
//                        .requestMatchers("api/**").hasRole(Role.USER.name())
                        // /about 요청에 대해서는 로그인을 요구함
                        .requestMatchers("/about").authenticated()
                        // /admin 요청에 대해서는 ROLE_ADMIN 역할을 가지고 있어야 함
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // 나머지 요청에 대해서는 로그인을 요구하지 않음
                        .anyRequest().permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(customOAuth2UserService))
//                )
                .formLogin(formlogin -> formlogin
                        .loginPage("/user/loginView")
                        .successForwardUrl("/index")
                        .failureForwardUrl("/index")
                        .permitAll()
                );


        return http.build();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
