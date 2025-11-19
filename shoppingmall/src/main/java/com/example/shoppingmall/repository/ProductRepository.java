package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// [수정] ProductRepositoryCustom 상속 추가
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}