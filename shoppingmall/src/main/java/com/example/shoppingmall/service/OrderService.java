package com.example.shoppingmall.service;

import com.example.shoppingmall.domain.*; // [수정] domain.*
import com.example.shoppingmall.dto.*; // [수정] dto.*
import com.example.shoppingmall.repository.OrderRepository;
import com.example.shoppingmall.repository.ProductRepository;
import com.example.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // [추가]

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 주문 생성 (기존 코드 ... 생략)
     */
    @Transactional
    public Long createOrder(OrderRequestDto requestDto, String userEmail) {
        // ... (기존 createOrder 메소드 내용은 그대로)
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자를 찾을 수 없습니다. email=" + userEmail));

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequestDto itemDto : requestDto.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다. id=" + itemDto.getProductId()));
            OrderItem orderItem = OrderItem.createOrderItem(
                    product,
                    product.getPrice(),
                    itemDto.getCount()
            );
            orderItems.add(orderItem);
        }
        Order order = Order.createOrder(user, orderItems);
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }

    // --- [ 1. '내 주문 목록 조회' 메소드 추가 ] ---

    /**
     * 내 주문 목록 조회
     */
    @Transactional(readOnly = true) // SELECT 전용
    public List<OrderResponseDto> getMyOrders(String userEmail) {
        // 1. 35단계에서 만든 쿼리 메소드로 주문 목록 조회
        List<Order> myOrders = orderRepository.findOrdersByUserEmail(userEmail);

        // 2. List<Order> -> List<OrderResponseDto> 변환
        return myOrders.stream()
                .map(OrderResponseDto::new) // order -> new OrderResponseDto(order)
                .collect(Collectors.toList());
    }

    // --- [ 2. '주문 취소' 메소드 추가 ] ---

    /**
     * 주문 취소
     */
    @Transactional // (중요) DB 변경(UPDATE, INSERT)이 발생하므로 readOnly = false
    public void cancelOrder(Long orderId, String userEmail) {
        // 1. 주문(Order) 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다. id=" + orderId));

        // 2. (중요!) 본인 확인
        //    주문한 사용자의 이메일과, 현재 인증된 사용자의 이메일이 같은지 확인
        if (!order.getUser().getEmail().equals(userEmail)) {
            // (보안) 다른 사람의 주문을 취소하려는 시도
            throw new IllegalStateException("주문을 취소할 권한이 없습니다.");
        }

        // 3. 주문 취소 로직 호출 (Order 엔티티 내부에서 처리)
        order.cancel();

        // (참고) @Transactional 어노테이션 덕분에,
        // 이 메소드가 끝나면 JPA가 'order' 엔티티의 변경(status=CANCEL)과
        // 'product' 엔티티의 변경(stockQuantity 증가)을
        // 감지(Dirty Checking)하여 자동으로 UPDATE 쿼리를 실행해 줍니다.
        // (orderRepository.save()를 호출할 필요 없음)
    }
}