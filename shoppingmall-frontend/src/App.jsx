import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import HomePage from './pages/HomePage'; // 1. 페이지 불러오기
import AuthPage from './pages/AuthPage'; // 2. 페이지 불러오기
import './App.css';
import './index.css'
// (참고) App.css에 네비게이션 바 스타일 추가
/* .navbar { background-color: #f0f0f0; padding: 1rem; }
.navbar a { margin-right: 1rem; text-decoration: none; color: #333; font-weight: bold; }
.navbar a:hover { color: #007bff; }
*/

function App() {
  const navigate = useNavigate(); // 페이지 이동을 위한 훅

  // (기존 App.jsx의 로그아웃 로직)
  const handleLogout = () => {
    localStorage.removeItem('token');
    alert('로그아웃 되었습니다.');
    navigate('/auth'); // 로그아웃 후 로그인 페이지로 이동
  };

  return (
    <div className="App">
      
      {/* 3. (★추가★) 네비게이션 바 (페이지 이동 링크) */}
      <nav className="navbar">
        {/* Link 태그는 a 태그처럼 동작하지만, 페이지 새로고침이 없음 */}
        <Link to="/">홈 (상품 목록)</Link>
        <Link to="/auth">로그인/회원가입</Link>
        {/* 로그아웃은 Link가 아닌 button으로 처리 */}
        <button onClick={handleLogout} className="logout-button-nav">
          로그아웃
        </button>
      </nav>

      <hr />

      {/* 4. (★핵심★) 페이지 라우팅 설정 */}
      {/* Routes: URL 주소에 맞는 Route를 하나만 찾아서 렌더링 */}
      <Routes>
        {/* path="/" : '홈' 주소일 때, <HomePage> 컴포넌트를 보여줌 */}
        <Route path="/" element={<HomePage />} /> 
        
        {/* path="/auth" : '/auth' 주소일 때, <AuthPage> 컴포넌트를 보여줌 */}
        <Route path="/auth" element={<AuthPage />} />
        
        {/* (고도화) /cart, /orders 등 다른 페이지도 여기에 추가 */}
      </Routes>

    </div>
  );
}

export default App;