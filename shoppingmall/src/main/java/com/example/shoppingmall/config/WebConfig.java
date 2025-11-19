package com.example.shoppingmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry; // [★추가★]
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 1. CORS 설정 (기존 유지)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("*")
                .maxAge(3600);
    }

    // --- [ ★ 2. 추가 ★: 이미지 정적 리소스 경로 매핑 ] ---
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 브라우저에서 'http://localhost:8080/images/파일명.jpg'로 요청하면
        // 로컬 PC의 'C:/shoppingmall-images/' 폴더에서 파일을 찾아서 보여준다.
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/shoppingmall-images/");
        // (주의) 맥/리눅스는 "file:///Users/..." 처럼 경로가 다름
    }
}