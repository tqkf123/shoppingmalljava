import { useState, useEffect } from 'react';
import api from '../api/axiosConfig';

function Cart() {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);

  // ( ... 기존 useEffect의 fetchCart 함수는 동일 ... )
  useEffect(() => {
    const fetchCart = async () => {
      try {
        setLoading(true);
        const response = await api.get('/api/cart');
        setCart(response.data);
      } catch (error) {
        console.error('장바구니 조회 실패:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchCart();
  }, []);

  // --- [ 1. ★추가★: '주문하기' 핸들러 함수 ] ---
  const handleOrder = async () => {
    // 장바구니가 비어있으면 실행하지 않음
    if (!cart || cart.cartItems.length === 0) {
      alert('장바구니가 비어있습니다. 상품을 먼저 담아주세요.');
      return;
    }

    try {
      // (★핵심★) 1. 장바구니(Cart) 데이터를 '주문 요청(OrderRequestDto)' 형식으로 변환
      //    OrderRequestDto는 { orderItems: [ { productId: 1, count: 2 }, ... ] } 형태임
      const orderRequestDto = {
        orderItems: cart.cartItems.map(item => ({
          productId: item.productId,
          count: item.count
        }))
      };

      // (★핵심★) 2. Axios로 백엔드 주문 API 호출
      //    (인터셉터가 자동으로 토큰 헤더 추가)
      const response = await api.post('/api/orders', orderRequestDto);

      // 3. (성공)
      alert('주문에 성공했습니다! 주문 ID: ' + response.data.split(': ')[1]);
      
      // 4. (고도화) 주문 성공 시, 장바구니를 비우기 위해
      //    현재 state를 업데이트하거나, fetchCart()를 다시 호출할 수 있습니다.
      setCart(null); // (가장 간단한 방법) 장바구니 state를 비움

    } catch (error) {
      // 5. (실패) (예: 주문 중 재고 부족)
      console.error('주문 실패:', error.response?.data || error.message);
      alert('주문 실패: ' + (error.response?.data || error.message));
    }
  };


  // ( ... 로딩 중 / 장바구니 비어있음 JSX는 동일 ... )
  if (loading) {
    return <div>장바구니를 불러오는 중입니다...</div>;
  }
  if (!cart || cart.cartItems.length === 0) {
    return <div>🛒 장바구니가 비어있습니다.</div>;
  }

  // --- [ 2. ★추가★: '주문하기' 버튼 ] ---
  return (
    <div className="cart-container">
      <h2>🛒 내 장바구니</h2>
      <div className="cart-item-list">
        {cart.cartItems.map(item => (
          <div key={item.productId} className="cart-item">
            {/* ... (기존 상품 정보) ... */}
          </div>
        ))}
      </div>
      
      {/* (★추가★) 주문하기 버튼 */}
      <button onClick={handleOrder} className="order-button">
        주문하기
      </button>
    </div>
  );
}

export default Cart;