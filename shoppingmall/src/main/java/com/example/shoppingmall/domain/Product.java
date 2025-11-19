package com.example.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // JPA 엔티티 선언
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products") // 'products' 테이블과 매핑
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id") // PK, 자동 증가
    private Long id;

    @Column(nullable = false, length = 200) // 상품명 (null 불가)
    private String name;

    @Column(nullable = false) // 가격 (null 불가)
    private Integer price;

    @Column(nullable = false) // 재고 수량 (null 불가)
    private Integer stockQuantity;

    @Column(length = 1000) // 상품 상세 설명
    private String description;

    // --- [ ★ 1. 수정 ★: 이미지 URL 필드 추가 ] ---
    @Column(length = 500) // 이미지 파일 경로 (예: /images/uuid-filename.jpg)
    private String imageUrl;
    // ------------------------------------

    @Builder
    // --- [ ★ 2. 수정 ★: 빌더에 imageUrl 추가 ] ---
    public Product(String name, Integer price, Integer stockQuantity, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.imageUrl = imageUrl; // [추가]
    }

    // --- [ ★ 3. 수정 ★: update 메소드에 imageUrl 추가 ] ---
    public void update(String name, Integer price, Integer stockQuantity, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.imageUrl = imageUrl; // [추가]
    }

    // (기존 재고 관리 메소드)
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new IllegalStateException("재고가 부족합니다. (현재 재고: " + this.stockQuantity + ")");
        }
        this.stockQuantity = restStock;
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
}