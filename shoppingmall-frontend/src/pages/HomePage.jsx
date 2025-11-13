import { useState, useEffect } from 'react';
import api from '../api/axiosConfig';
import Cart from '../components/Cart'; // Cart 컴포넌트

function HomePage() {
  const [products, setProducts] = useState([]);

  // 1. 상품 목록 불러오기 (기존 App.jsx의 useEffect)
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await api.get('/api/products');
        setProducts(response.data);
      } catch (error) {
        console.error('상품 목록 로딩 실패:', error);
      }
    };
    fetchProducts();
  }, []);

  // 2. 장바구니 담기 (기존 App.jsx의 handleAddToCart)
  const handleAddToCart = async (productId) => {
    const requestDto = { productId: productId, count: 1 };
    try {
      await api.post('/api/cart', requestDto);
      alert('장바구니에 상품을 담았습니다!');
      // (고도화) Cart 컴포넌트가 실시간으로 업데이트되게 하려면
      // state를 App.jsx 최상단으로 올리고 props로 내려줘야 함 (지금은 생략)
    } catch (error) {
      if (error.response && error.response.status === 401) {
        alert('로그인이 필요합니다. (로그인 페이지로 이동시켜야 함)');
      } else {
        alert('장바구니 추가 실패: ' + (error.response?.data || error.message));
      }
    }
  };

  return (
    <div>
      {/* 3. 장바구니 컴포넌트 표시 */}
      <Cart />
      <hr />
      
      {/* 4. 상품 목록 표시 */}
      <h2>🛒 상품 목록</h2>
      <div className="product-list">
        {products.map(product => (
          <div key={product.id} className="product-item">
            <h3>{product.name}</h3>
            <p>가격: {product.price}원</p>
            <p>재고: {product.stockQuantity}개</p>
            <p>{product.description}</p>
            <button onClick={() => handleAddToCart(product.id)}>
              장바구니 담기
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default HomePage;