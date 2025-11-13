import { useState } from 'react';
import api from '../api/axiosConfig'; // [2. ★추가★]

function Login() {
  // 1. (메모리) email, password state
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });

  // 2. (이벤트) input 값 변경 시 state 업데이트
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value
    }));
  };

  // 3. (이벤트) '로그인' 버튼 클릭 시 실행
  const handleSubmit = async (e) => {
    e.preventDefault(); // 새로고침 방지

    try {
      // 4. (★수정★) 'axios.post' -> 'api.post'
      const response = await api.post(
        '/api/users/login',
        formData
      );

      // 5. (★성공★) 응답으로 받은 JWT 토큰(accessToken)을 
      //    브라우저의 'localStorage'에 저장합니다.
      const { accessToken } = response.data;
      localStorage.setItem('token', accessToken); // 'token'이라는 이름으로 저장

      console.log('로그인 성공! 토큰:', accessToken);
      alert('로그인에 성공했습니다!');

      // (참고) 로그인 성공 후, 메인 페이지로 이동(redirect)하는 로직 추가 가능

    } catch (error) {
      // 6. (실패)
      console.error('로그인 실패:', error.response?.data || error.message);
      alert('로그인 실패: ' + (error.response?.data || "이메일 또는 비밀번호를 확인하세요."));
    }
  };

  // 7. (그리기) 화면(JSX)
  return (
    <div className="form-container">
      <h2>로그인</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>이메일:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>비밀번호:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">로그인</button>
      </form>
    </div>
  );
}

export default Login;