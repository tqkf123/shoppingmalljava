import { Routes, Route, Link, useNavigate } from 'react-router-dom';

// 1. 페이지 컴포넌트 Import
import HomePage from './pages/HomePage';
import AuthPage from './pages/AuthPage';
import CartPage from './pages/CartPage';
import ProductDetailPage from './pages/ProductDetailPage';
import OrderHistoryPage from './pages/OrderHistoryPage';
import AdminProductPage from './pages/AdminProductPage'; // [★ 관리자 페이지]

// 2. 라이브러리 Import
import { Toaster, toast } from 'react-hot-toast';
import { useRecoilValue, useSetRecoilState } from 'recoil';

// 3. 전역 상태(Atom) Import
import { authState } from './store/authState';
import { cartState } from './store/cartState';

import './App.css';

function App() {
  // 4. 훅 초기화
  const navigate = useNavigate();
  const setGlobalCart = useSetRecoilState(cartState);
  const setGlobalAuth = useSetRecoilState(authState);
  
  // (★수정★) authState는 이제 {isAuth, role, email} 객체
  const auth = useRecoilValue(authState); 
  const isAuth = auth.isAuth; // 로그인 여부 (true/false)
  const isAdmin = auth.role === 'ADMIN'; // 관리자 여부 (true/false)

  // 5. 로그아웃 핸들러
  const handleLogout = () => {
    localStorage.removeItem('token');
    setGlobalCart(null);   
    setGlobalAuth({ isAuth: false, role: null, email: null }); // (★수정★) 객체로 초기화
    toast.success('로그아웃 되었습니다.');
    navigate('/auth'); 
  };

  // 6. JSX 렌더링
  return (
    <div className="min-h-screen bg-gray-50">
      
      <Toaster 
        position="top-center"
        reverseOrder={false}
      />

      {/* --- 네비게이션 바 (공통 헤더) --- */}
      <nav className="bg-white shadow-md p-4 flex justify-between items-center sticky top-0 z-50">
        <div className="text-xl font-bold text-gray-800">
            <Link to="/">🛍️ MyShop</Link>
        </div>
        
        {/* --- (★수정★) 로그인 상태 + 관리자 권한에 따른 메뉴 분기 --- */}
        <div className="flex items-center space-x-4">
          <Link to="/" className="text-gray-600 hover:text-blue-600 transition duration-150">홈</Link>
          
          {/* (관리자 전용) */}
          {isAdmin && (
             <Link to="/admin/products" className="text-red-600 font-bold hover:text-red-800 transition duration-150">
                상품 관리
             </Link>
          )}
          
          {isAuth ? (
            /* --- (로그인 됨) --- */
            <>
              <Link to="/orders" className="text-gray-600 hover:text-blue-600 transition duration-150">
                주문 내역
              </Link> 
              <Link to="/cart" className="text-gray-600 hover:text-blue-600 transition duration-150">
                장바구니
              </Link> 
              <button 
                onClick={handleLogout} 
                className="bg-red-500 hover:bg-red-600 text-white font-bold py-1 px-3 rounded text-sm transition duration-150"
              >
                로그아웃
              </button>
            </>
          ) : (
            /* --- (로그아웃 됨) --- */
            <>
              <Link to="/cart" className="text-gray-600 hover:text-blue-600 transition duration-150">
                장바구니
              </Link> 
              <Link to="/auth" className="text-blue-600 hover:text-blue-800 font-semibold transition duration-150">
                로그인/가입
              </Link>
            </>
          )}
        </div>
        {/* ------------------------------------ */}
      </nav>

      {/* --- 페이지 본문 (라우팅 영역) --- */}
      <main className="p-4 md:p-8">
        <Routes>
          <Route path="/" element={<HomePage />} /> 
          <Route path="/auth" element={<AuthPage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/products/:productId" element={<ProductDetailPage />} />
          <Route path="/orders" element={<OrderHistoryPage />} />
          
          {/* (★추가★) 관리자 페이지 라우트 */}
          <Route path="/admin/products" element={<AdminProductPage />} /> 
        </Routes>
      </main>

    </div>
  );
}

export default App;