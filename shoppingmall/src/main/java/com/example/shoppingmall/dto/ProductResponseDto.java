package com.example.shoppingmall.dto;

import com.example.shoppingmall.domain.Product;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private final Long id;
    private final String name;
    private final Integer price;
    private final Integer stockQuantity;
    private final String description;

    // Entity를 DTO로 변환하는 생성자 (Service에서 사용)
    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.description = product.getDescription();
    }
}