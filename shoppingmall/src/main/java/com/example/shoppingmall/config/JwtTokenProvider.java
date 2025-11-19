package com.example.shoppingmall.config;

import com.example.shoppingmall.domain.UserRoleEnum; // [★ 1. import 추가 ★]
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private long tokenValidityInMilliseconds;

    private Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰 생성
     */
    // --- [ ★ 2. 수정 ★: userEmail, userRole 받기 ] ---
    public String createToken(String userEmail, UserRoleEnum userRole) {
        Claims claims = Jwts.claims().setSubject(userEmail);

        // (★핵심★) 'claims'에 권한 정보("role") 추가
        claims.put("role", userRole.name()); // "USER" 또는 "ADMIN" 문자열 저장

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // ------------------------------------

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

    // --- [ ★ 3. 추가 ★: 토큰에서 권한 정보 추출 ] ---
    public String getUserRole(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class); // "role" 클레임 값을 String으로 추출
        } catch (Exception e) {
            // (토큰이 유효하지 않을 경우 등)
            return null;
        }
    }
    // ------------------------------------

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