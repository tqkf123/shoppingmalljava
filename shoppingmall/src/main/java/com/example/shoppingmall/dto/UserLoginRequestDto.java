package com.example.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // JSON -> 객체 변환
public class UserLoginRequestDto {
    private String email;
    private String password;
}