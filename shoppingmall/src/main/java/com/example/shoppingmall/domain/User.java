package com.example.shoppingmall.domain;

import jakarta.persistence.*; // jakarta.persistence.* 로 임포트해야 합니다!
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // 이 클래스가 JPA 엔티티임을 선언합니다.
@Getter // Lombok: 모든 필드에 대한 Getter를 자동으로 생성합니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Lombok: 기본 생성자를 만듭니다. (JPA는 기본 생성자가 필요합니다)
@Table(name = "users") // 데이터베이스에 'users'라는 이름의 테이블로 생성됩니다.
public class User {

    @Id // 이 필드가 테이블의 Primary Key(기본 키)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 값을 DB가 자동으로 생성(Auto-increment)하도록 합니다.
    @Column(name = "user_id") // 테이블의 컬럼 이름을 'user_id'로 지정합니다.
    private Long id;

    @Column(nullable = false, unique = true, length = 50) // null 불가, 유니크 제약조건, 길이 50
    private String email; // 이메일 (로그인 ID로 사용)

    @Column(nullable = false, length = 100) // null 불가, 길이 100
    private String password; // 비밀번호

    @Column(nullable = false, length = 30) // null 불가, 길이 30
    private String name; // 사용자 이름

    @Column(length = 100) // 주소
    private String address;


    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Cart cart;


    // (참고) @Builder는 생성자를 통해 객체를 안전하게 생성하기 위해 사용합니다.
    @Builder
    public User(String email, String password, String name, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
    }
}