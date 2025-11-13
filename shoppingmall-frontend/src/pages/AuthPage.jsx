import Login from '../components/Login';
import SignUp from '../components/SignUp';
// (CSS를 위해 App.css를 임포트)
import '../App.css'; 

function AuthPage() {
  return (
    <div>
      <h1>🛍️ 우리 쇼핑몰</h1>
      <div className="auth-container">
        <SignUp />
        <Login />
      </div>
      <p style={{textAlign: 'center', marginTop: '20px'}}>
        로그인 또는 회원가입을 진행해주세요.
      </p>
    </div>
  );
}

export default AuthPage;