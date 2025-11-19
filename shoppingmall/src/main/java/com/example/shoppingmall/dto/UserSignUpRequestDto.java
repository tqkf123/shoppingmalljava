package com.example.shoppingmall.dto;

import com.example.shoppingmall.domain.User;
import lombok.Getter;
import lombok.Setter;

// Lombok 어노테이션
@Getter
@Setter // 컨트롤러가 JSON 데이터를 이 객체로 변환할 때 필요합니다.
public class UserSignUpRequestDto {

    private String email;
    private String password;
    private String name;
    private String address;

    // (선택) DTO를 Entity로 변환하는 메소드
    // 이 DTO 객체를 User 엔티티 객체로 바꿔서 DB에 저장할 때 사용합니다.
    public User toEntity() {
        return User.builder()
                .email(this.email)
                .password(this.password) // ★주의: 비밀번호는 암호화가 필요합니다!
                .name(this.name)
                .address(this.address)
                .build();
    }
}