package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// Product 엔티티를 관리하고, PK 타입은 Long
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository<Product, Long>를 상속받는 것만으로
    // save(), findById(), findAll(), delete() 등
    // 상품 CRUD에 필요한 기본 기능이 *모두* 자동 구현됩니다.
    // (User와 달리 '이름'으로 검색 등은 일단 필요 없으니 비워둡니다.)
}