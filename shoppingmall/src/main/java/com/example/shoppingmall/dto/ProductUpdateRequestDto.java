package com.example.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // JSON -> 객체 변환
public class ProductUpdateRequestDto {

    // [참고] 상품 수정 시, 어떤 필드를 수정 가능하게 할지 정의합니다.
    //      여기서는 모든 필드를 수정 가능하다고 가정합니다.
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private String description;
}