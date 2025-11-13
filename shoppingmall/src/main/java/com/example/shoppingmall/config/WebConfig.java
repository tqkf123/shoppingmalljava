package com.example.shoppingmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Spring의 설정 파일임을 선언
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 1. (중요) "모든" 경로(/...)에 대해
                .addMapping("/**")

                // 2. (중요) "http://localhost:5173" (프론트 서버)로부터의 요청을
                .allowedOrigins("http://localhost:5173")

                // 3. 허용할 HTTP 메소드 (GET, POST, PUT, DELETE 등)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")

                // 4. (중요) 인증 헤더(Authorization - JWT 토큰)를 포함한 요청 허용
                .allowCredentials(true)

                // 5. 허용할 헤더
                .allowedHeaders("*")

                // 6. (선택) Preflight 요청의 캐시 시간 (초)
                .maxAge(3600);
    }
}