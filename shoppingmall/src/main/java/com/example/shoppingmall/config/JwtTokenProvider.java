package com.example.shoppingmall.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component // Spring의 Bean으로 등록
public class JwtTokenProvider {

    @Value("${jwt.secret}") // application.properties의 secret 키
    private String secretKey;

    @Value("${jwt.expiration-ms}") // application.properties의 만료 시간
    private long tokenValidityInMilliseconds;

    private Key key;

    @PostConstruct // 의존성 주입 후 초기화 수행
    protected void init() {
        // secretKey를 Base64 디코딩
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        // HMAC SHA 키 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰 생성
     */
    public String createToken(String userEmail) {
        Claims claims = Jwts.claims().setSubject(userEmail); // 토큰의 주체(subject)
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds); // 만료 시간

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now)  // 토큰 발행 시간
                .setExpiration(validity) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘
                .compact(); // 토큰 생성
    }

    /**
     * JWT 토큰에서 사용자 이메일 추출
     */
    public String getUserEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * JWT 토큰 유효성 + 만료일자 확인
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 유효하지 않은 토큰, 만료된 토큰 등
            return false;
        }
    }
}