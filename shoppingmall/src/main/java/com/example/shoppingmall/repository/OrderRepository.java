package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // [추가]

public interface OrderRepository extends JpaRepository<Order, Long> {

    // --- [ 아래 메소드 추가 ] ---

    /**
     * (중요) JPQL(JPA Query Language)을 사용한 쿼리
     * 특정 사용자의 주문 목록을 주문 날짜(orderDate) 내림차순(DESC)으로 조회합니다.
     * * @Query("SELECT o FROM Order o WHERE o.user.email = :email ORDER BY o.orderDate DESC")
     * - 'Order'는 엔티티 이름 (테이블 이름 orders 아님)
     * - 'o.user.email' : Order 엔티티가 가진 user 필드의 email 필드
     * - ':email' : 파라미터로 받은 email 변수
     *
     * (참고) Spring Data JPA는 메소드 이름(예: findAllByUser_EmailOrderByOrderDateDesc)으로도
     * 쿼리를 자동 생성할 수 있지만, 복잡해지면 @Query를 쓰는 것이 명확합니다.
     */
    @org.springframework.data.jpa.repository.Query("SELECT o FROM Order o WHERE o.user.email = :email ORDER BY o.orderDate DESC")
    List<Order> findOrdersByUserEmail(@org.springframework.data.repository.query.Param("email") String email);
}