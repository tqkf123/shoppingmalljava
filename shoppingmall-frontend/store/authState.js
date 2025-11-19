import { atom } from 'recoil';
import { jwtDecode } from 'jwt-decode'; // (★설치 필요: npm install jwt-decode)

/**
 * 앱 시작 시 localStorage의 토큰을 읽어
 * 사용자의 로그인 상태(isAuth), 권한(role), 이메일(email)을
 * Recoil Atom의 초기값(default)으로 설정합니다.
 */
const getInitialAuthState = () => {
  // 1. localStorage에서 토큰을 가져옵니다.
  const token = localStorage.getItem('token');
  
  // 2. 토큰이 없으면 로그아웃 상태입니다.
  if (!token) {
    return { isAuth: false, role: null, email: null };
  }
  
  try {
    // 3. (★핵심★) 토큰을 디코딩(해독)해서 내부 정보(payload)를 읽습니다.
    const decodedToken = jwtDecode(token);
    
    // 4. (★핵심★) 백엔드(JwtTokenProvider)에서 넣어준 "role"과 "sub"(이메일) 추출
    const userRole = decodedToken.role;
    const userEmail = decodedToken.sub; // 'sub'는 subject(주제)의 약자

    // 5. 토큰 만료 시간 체크 (exp는 초 단위)
    const isExpired = decodedToken.exp * 1000 < Date.now();
    
    if (isExpired) {
        // 만료되었으면 토큰을 삭제하고 로그아웃 상태 반환
        console.warn("만료된 토큰입니다. 로그아웃합니다.");
        localStorage.removeItem('token');
        return { isAuth: false, role: null, email: null };
    }
    
    // 6. 토큰이 유효하면 로그인 상태 + 권한 정보 반환
    return { isAuth: true, role: userRole, email: userEmail };
    
  } catch (error) {
    // 7. 토큰이 유효하지 않은 경우 (예: 깨진 토큰)
    console.error("잘못된 토큰입니다:", error);
    localStorage.removeItem('token');
    return { isAuth: false, role: null, email: null };
  }
};

// 전역 인증 상태 Atom
export const authState = atom({
  key: 'authState', // 앱 전체에서 고유한 ID
  default: getInitialAuthState(), // 위 함수를 실행한 결과가 기본값
});