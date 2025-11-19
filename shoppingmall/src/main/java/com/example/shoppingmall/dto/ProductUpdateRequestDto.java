package com.example.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // JSON -> 객체 변환
public class ProductUpdateRequestDto {

    // (이 파일은 변경 사항 없음)

    private String name;
    private Integer price;
    private Integer stockQuantity;
    private String description;
}