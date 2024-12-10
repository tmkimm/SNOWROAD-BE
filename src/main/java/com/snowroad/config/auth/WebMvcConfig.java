package com.snowroad.config.auth;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig  implements WebMvcConfigurer {
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
}
