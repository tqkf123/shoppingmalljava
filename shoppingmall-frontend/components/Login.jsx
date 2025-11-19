import { useState } from 'react';
import api from '../api/axiosConfig';
import { useSetRecoilState } from 'recoil';
import { cartState } from '../store/cartState';
import { authState } from '../store/authState'; // authState import
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast'; // toast import
import { jwtDecode } from 'jwt-decode'; // 토큰 해독용

// MUI 컴포넌트
import { Button, TextField, Box, Typography } from '@mui/material';

function Login() {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });

  // Recoil Setter와 Navigate 훅
  const setGlobalCart = useSetRecoilState(cartState);
  const setGlobalAuth = useSetRecoilState(authState); // authState setter
  const navigate = useNavigate();

  // input 값 변경 시 state 업데이트
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value
    }));
  };

  // '로그인' 버튼 클릭 시 실행
  const handleSubmit = async (e) => {
    e.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
    
    try {
      // 1. 로그인 API 호출
      const response = await api.post('/api/users/login', formData);
      const { accessToken } = response.data;
      
      // 2. 토큰을 localStorage에 저장
      localStorage.setItem('token', accessToken);

      // 3. (★핵심★) 토큰을 즉시 디코딩하여 권한(role)과 이메일(sub) 추출
      const decodedToken = jwtDecode(accessToken);
      const userRole = decodedToken.role;
      const userEmail = decodedToken.sub;

      // 4. (★핵심★) 전역 authState를 '로그인 상태'로 업데이트
      setGlobalAuth({ isAuth: true, role: userRole, email: userEmail });

      // 5. (★핵심★) 로그인 성공 직후, 장바구니 정보 API 호출
      const cartResponse = await api.get('/api/cart');
      
      // 6. (★핵심★) 전역 cartState에 장바구니 정보 저장
      setGlobalCart(cartResponse.data);

      toast.success('로그인에 성공했습니다!');
      
      // 7. 로그인 성공 시 홈('/') 페이지로 이동
      navigate('/'); 

    } catch (error) {
      console.error('로그인 실패:', error.response?.data || error.message);
      toast.error('로그인 실패: ' + (error.response?.data || "이메일 또는 비밀번호를 확인하세요."));
      
      // (실패 시 혹시 모를 쓰레기 값 제거)
      setGlobalAuth({ isAuth: false, role: null, email: null });
    }
  };

  // 화면(JSX)
  return (
    <Box 
        component="form" 
        onSubmit={handleSubmit} 
        className="form-container" 
        sx={{ '& .MuiTextField-root': { m: 1, width: '100%' } }}
    >
      <Typography variant="h6" component="h2" className="text-xl font-semibold mb-4 text-center">
        로그인
      </Typography>
      
      <TextField
        label="이메일"
        type="email"
        name="email"
        variant="outlined"
        value={formData.email}
        onChange={handleChange}
        required
      />
      
      <TextField
        label="비밀번호"
        type="password"
        name="password"
        variant="outlined"
        value={formData.password}
        onChange={handleChange}
        required
      />

      <Button 
        type="submit" 
        variant="contained" 
        color="primary" 
        fullWidth
        sx={{ mt: 2, p: 1.5, m: 1, width: '100%' }}
      >
        로그인
      </Button>
    </Box>
  );
}

export default Login;