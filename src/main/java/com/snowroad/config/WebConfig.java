package com.snowroad.config;

import com.snowroad.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig  implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/static/", "classpath:/public/", "classpath:/",
            "classpath:/resources/", "classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/" };

    @Override
    // 뷰 컨트롤러 추가
    public void addViewControllers(ViewControllerRegistry registry) {
        // 루트 경로로 요청이 들어오면 /index 페이지로 forward한다.
        // 리다이렉트가 아니라 포워드 방식으로 /index url로 전달한다.
        //registry.addViewController( "/" ).setViewName( "forward:/index" );
        // 우선순위를 가장 높게 잡는다.
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    // 정적 자원을 처리하는 리소스 핸들러를 추가한다.
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /로 시작하는 모든 요청에 대해 정적 자원을 처리하는 핸들러를 추가한다.
        // addResourceLocations는 정적 자원을 어디에서 찾을지 설정한다.
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 요청에 대해 CORS 허용
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000"
                        , "http://localhost:3000/"
                        , "http://127.0.0.1:3000"
                        , "http://ec2-13-125-216-97.ap-northeast-2.compute.amazonaws.com"
                        , "http://noongil.org"
                        , "https://noongil.org"
                        , "http://ec2-3-36-23-213.ap-northeast-2.compute.amazonaws.com:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 자격 증명(쿠키 등) 허용
                .maxAge(3600); // CORS preflight 요청을 위한 캐시 시간 (초)
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolver) {
        argumentResolver.add(loginUserArgumentResolver);
    }
}
