package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.UserLoginRequestDto; // [추가]
import com.example.shoppingmall.dto.UserLoginResponseDto; // [추가]
import com.example.shoppingmall.dto.UserSignUpRequestDto;
import com.example.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserSignUpRequestDto requestDto) {
        // (기존 코드 ... 생략)
        try {
            Long savedUserId = userService.signUp(requestDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("회원가입 성공! ID: " + savedUserId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // --- [ 아래 'login' 메소드 추가 ] ---

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto requestDto) {
        try {
            // 서비스 로직 호출 (로그인)
            UserLoginResponseDto responseDto = userService.login(requestDto);

            // 성공 시: 200 OK 상태 코드와 JWT 토큰이 담긴 DTO 반환
            return ResponseEntity.ok(responseDto); // .ok() == .status(HttpStatus.OK)

        } catch (IllegalArgumentException e) {
            // 실패 시 (예: 이메일 없음, PW 불일치): 400 Bad Request
            // (보안을 위해 "이메일 또는 비밀번호가 잘못되었습니다."처럼
            //  하나의 메시지로 퉁치는 것이 더 좋습니다.)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build(); // 에러 메시지 대신 body 없이 400 상태만 반환
        }
    }
}