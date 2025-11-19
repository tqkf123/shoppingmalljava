package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // (기존) 유저 ID로 찾기
    Optional<Cart> findByUserId(Long userId);

    // (수정) 이메일로 찾기 -> Fetch Join 적용!
    @Query(
            "SELECT DISTINCT c FROM Cart c " +
                    "LEFT JOIN FETCH c.cartItems ci " + // 1. Cart와 CartItem 즉시 로딩
                    "LEFT JOIN FETCH ci.product p " +   // 2. CartItem과 Product 즉시 로딩
                    "WHERE c.user.email = :email"
    )
    Optional<Cart> findByUserEmail(@Param("email") String email);
}