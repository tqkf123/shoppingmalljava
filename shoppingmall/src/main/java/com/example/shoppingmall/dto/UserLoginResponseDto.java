package com.example.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자
public class UserLoginResponseDto {
    private String accessToken; // JWT 토큰
}