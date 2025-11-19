package com.example.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders") // 'order'는 SQL 예약어일 수 있으므로 'orders' 사용
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // (중요) '누가' 주문했는지 (User와 연관관계)
    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계. 주문(N) : 회원(1)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키(FK)
    private User user;

    @Column(nullable = false)
    private LocalDateTime orderDate; // 주문 날짜

    @Enumerated(EnumType.STRING) // Enum 타입을 문자열로 저장
    @Column(nullable = false, length = 10)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    // (중요) '무엇을' 주문했는지 (OrderItem과 연관관계)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    // 1:N 관계. 주문(1) : 주문상품(N)
    // cascade = CascadeType.ALL : Order를 저장/삭제할 때 OrderItem도 함께 저장/삭제
    // orphanRemoval = true : 부모(Order)와 연결이 끊어진 자식(OrderItem)을 자동 삭제
    private List<OrderItem> orderItems = new ArrayList<>();

    // == 생성자 == //
    // (JPA 엔티티는 빌더 패턴보다 정적 팩토리 메소드나 생성자를 통한 관리를 선호하기도 합니다)
    public static Order createOrder(User user, List<OrderItem> orderItems) {
        Order order = new Order();
        order.user = user;
        order.orderDate = LocalDateTime.now(); // 주문 시간
        order.status = OrderStatus.ORDER; // 주문 상태

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    // == 연관관계 편의 메소드 == //
    // (Order에 OrderItem을 추가할 때, OrderItem에도 Order를 설정해줌)
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void cancel() {
        // (방어 로직) 이미 배송 완료된 상품은 취소가 불가능하다는 등의 로직 추가 가능
        // if (status == DeliveryStatus.COMP) {
        //     throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        // }

        // 1. 주문 상태를 'CANCEL'로 변경
        this.status = OrderStatus.CANCEL;

        // 2. (중요) 이 주문에 포함된 모든 OrderItem을 순회하며
        //    각각의 'cancel()' 메소드를 호출 (재고 원복)
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // 16단계에서 만든 재고 원복 로직 호출
        }
    }
}