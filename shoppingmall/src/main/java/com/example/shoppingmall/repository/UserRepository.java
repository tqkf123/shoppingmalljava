package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository를 상속받습니다.
// <User, Long>은 이 리포지토리가 User 엔티티를 관리하며,
// 그 엔티티의 PK(기본 키) 타입이 Long임을 의미합니다.
public interface UserRepository extends JpaRepository<User, Long> {

    // JpaRepository가 기본으로 제공하는 메소드:
    // save(), findById(), findAll(), count(), delete() 등...

    // 우리는 '이메일'로 사용자를 찾는 기능이 필요합니다.
    // JPA는 메소드 이름을 분석하여 자동으로 SQL 쿼리를 생성해 줍니다.
    // "SELECT * FROM users WHERE email = ?" 쿼리가 자동으로 생성됩니다.
    Optional<User> findByEmail(String email);
}