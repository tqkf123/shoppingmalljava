package com.example.shoppingmall.dto;

import com.example.shoppingmall.domain.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // JSON -> 객체 변환을 위해 필요
public class ProductCreateRequestDto {

    private String name;
    private Integer price;
    private Integer stockQuantity;
    private String description;

    // DTO를 Entity로 변환하는 메소드 (Service에서 사용)
    public Product toEntity() {
        return Product.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .description(description)
                .build();
    }
}