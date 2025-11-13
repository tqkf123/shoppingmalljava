package com.example.shoppingmall.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // [중요]
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // [중요]

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 29단계에서 만든 JwtAuthenticationFilter 주입
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 7단계에서 만든 PasswordEncoder Bean (그대로 유지)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보호 비활성화 (JWT 사용 시 불필요)
                .csrf(csrf -> csrf.disable())

                // 2. (중요) HTTP 기본 인증(Basic Auth) 및 폼 로그인(Form Login) 비활성화
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

                // 3. (중요) 세션 관리 정책 설정: STATELESS (세션을 사용하지 않음)
                //    JWT는 세션 대신 토큰을 사용하므로 STATELESS로 설정해야 함
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. (중요) API 경로별 권한 설정
                .authorizeHttpRequests(authz -> authz

                        // --- [ (★이 줄을 추가★) ] ---
                        // (CORS Preflight) OPTIONS 메소드는 '인증 없이' 항상 허용
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // 1. 인증 없이 접근 허용 (permitAll)
                        .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products", "/api/products/**").permitAll()

                        // 2. 인증(로그인)이 필요한 API
                        // ( ... 나머지 authenticated() 설정들 ... )
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/products").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/products/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/products/**").authenticated()
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/api/cart/**").authenticated()

                        .anyRequest().authenticated()
                )

                // 5. (핵심) JWT 필터 추가
                // Spring Security의 기본 인증 필터(UsernamePasswordAuthenticationFilter) '이전에'
                // 우리가 만든 jwtAuthenticationFilter를 실행하도록 설정
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}