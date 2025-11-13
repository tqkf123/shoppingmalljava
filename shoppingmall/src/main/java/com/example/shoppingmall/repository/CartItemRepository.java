package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // (핵심) 특정 장바구니(cartId)에 특정 상품(productId)이
    // 이미 담겨 있는지 확인하는 쿼리 메소드
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}