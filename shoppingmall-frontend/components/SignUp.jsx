import { useState } from 'react';
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast'; // [★ 1. Toast import]

// [★ 2. MUI 컴포넌트 import]
import { Button, TextField, Box, Typography } from '@mui/material';

function SignUp() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    name: '',
    address: ''
  });
  
  const navigate = useNavigate();

  // input 값 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value
    }));
  };

  // 폼 제출 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // API 호출
      await api.post('/api/users/signup', formData);

      // [★ 3. alert() -> toast.success()로 수정 ★]
      toast.success('회원가입 성공! 로그인 해주세요.');
      
      // 회원가입 성공 시 로그인 페이지로 이동
      navigate('/auth'); 

    } catch (error) {
      // [★ 4. alert() -> toast.error()로 수정 ★]
      toast.error('회원가입 실패: ' + (error.response?.data || error.message));
    }
  };

  // [★ 5. JSX 부분을 MUI 컴포넌트로 수정 ★]
  return (
    <Box 
        component="form" 
        onSubmit={handleSubmit} 
        className="form-container" 
        // (MUI sx prop: m(margin) 1, width 100%를 모든 TextField에 적용)
        sx={{ '& .MuiTextField-root': { m: 1, width: '100%' } }}
    >
      <Typography variant="h6" component="h2" className="text-xl font-semibold mb-4 text-center">
        회원가입
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

      <TextField
        label="이름"
        type="text"
        name="name"
        variant="outlined"
        value={formData.name}
        onChange={handleChange}
        required
      />

      <TextField
        label="주소"
        type="text"
        name="address"
        variant="outlined"
        value={formData.address}
        onChange={handleChange}
      />

      <Button 
        type="submit" 
        variant="contained" 
        color="success" // (로그인과 색상 구분)
        fullWidth
        sx={{ mt: 2, p: 1.5, m: 1, width: '100%' }} // 마진(m:1) 추가
      >
        가입하기
      </Button>
    </Box>
  );
}

export default SignUp;