import { useState } from 'react';
import api from '../api/axiosConfig'; // [2. ★추가★] (경로 주의)

function SignUp() {
  // 1. (메모리) 폼의 입력값(email, password 등)을 저장할 'state'
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    name: '',
    address: ''
  });

  // 2. (이벤트) input 값이 변경될 때마다 'state'를 업데이트하는 함수
  const handleChange = (e) => {
    // 'e.target'은 이벤트가 발생한 <input> 태그
    // 'name'은 <input name="...">, 'value'는 <input value="...">
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData, // 기존 formData 데이터를 복사하고
      [name]: value // [name] (예: 'email') 키의 값을 'value'로 덮어쓰기
    }));
  };

  // 3. (이벤트) '회원가입' 버튼 클릭 시(폼 제출) 실행되는 함수
  const handleSubmit = async (e) => {
    e.preventDefault(); // 폼 제출 시 브라우저가 새로고침되는 기본 동작을 막음

    try {
      // 4. (★수정★) 'axios.post' -> 'api.post'
      //    (baseURL이 설정되어 있으므로 '/api/users/signup'만 씀)
      const response = await api.post(
        '/api/users/signup', 
        formData
      );

      // 5. (성공)
      console.log('회원가입 성공:', response.data);
      alert(response.data); // "회원가입 성공! ID: ..." 알림창 띄우기

    } catch (error) {
      // 6. (실패)
      console.error('회원가입 실패:', error.response?.data || error.message);
      // 'error.response.data'에 백엔드가 보낸 에러 메시지(예: "이미 사용 중인 이메일")가 담겨있음
      alert('회원가입 실패: ' + (error.response?.data || error.message));
    }
  };

  // 7. (그리기) 화면(JSX)
  return (
    <div className="form-container">
      <h2>회원가입</h2>
      {/* 3번 handleSubmit 함수를 폼 제출 이벤트에 연결 */}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>이메일:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange} // 2번 handleChange 함수 연결
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
        <div className="form-group">
          <label>이름:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>주소:</label>
          <input
            type="text"
            name="address"
            value={formData.address}
            onChange={handleChange}
          />
        </div>
        <button type="submit">가입하기</button>
      </form>
    </div>
  );
}

export default SignUp;