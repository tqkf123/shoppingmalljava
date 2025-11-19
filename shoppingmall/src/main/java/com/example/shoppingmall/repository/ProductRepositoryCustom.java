package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    // 검색어(keyword)로 상품을 검색하고, 페이징(Pageable) 처리하여 반환
    Page<Product> searchProducts(String keyword, Pageable pageable);
}