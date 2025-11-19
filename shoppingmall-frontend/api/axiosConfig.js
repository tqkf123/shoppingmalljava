import axios from 'axios';

// 1. (★) Axios 인스턴스 생성
const api = axios.create({
  // (★) 백엔드 서버의 주소를 기본 URL로 설정
  baseURL: 'http://localhost:8080', 
  headers: {
    'Content-Type': 'application/json',
  },
});

// 2. (★) 요청 인터셉터(Request Interceptor) 추가
//    : 모든 API 요청이 서버로 전송되기 *전에* 이 코드가 실행됩니다.
api.interceptors.request.use(
  (config) => {
    // 3. localStorage에서 'token'을 가져옵니다.
    const token = localStorage.getItem('token');

    if (token) {
      // 4. 토큰이 있다면, 'Authorization' 헤더에 'Bearer [토큰]' 형식으로 추가
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    
    // 5. 설정이 완료된 config를 반환하여 요청을 계속 진행
    return config;
  },
  (error) => {
    // 요청 오류 처리
    return Promise.reject(error);
  }
);

export default api; // 6. 설정이 완료된 Axios 인스턴스를 export