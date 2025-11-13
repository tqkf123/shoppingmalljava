package com.example.shoppingmall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 이 클래스가 REST API 컨트롤러임을 선언
public class HelloController {

    @GetMapping("/api/hello") // HTTP GET 요청이 /api/hello 주소로 올 때
    public String hello() {
        return "안녕하세요! 쇼핑몰 백엔드 서버입니다!"; // 이 문자열을 응답으로 반환
    }
}