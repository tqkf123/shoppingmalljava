package com.example.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 100)
    private String address;

    // --- [ 권한 필드 ] ---
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) // DB에 "USER", "ADMIN" 문자열로 저장
    private UserRoleEnum role;

    // --- [ 생성자 (Builder) ] ---
    // ★중요★: 파라미터에 UserRoleEnum role을 받고, 내부에서 this.role = role; 을 꼭 해줘야 합니다.
    @Builder
    public User(String email, String password, String name, String address, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.role = role; // [핵심] 이 줄이 없으면 DB 에러가 발생합니다.
    }
}