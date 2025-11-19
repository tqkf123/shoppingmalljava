import { atom } from 'recoil';

// (참고) Atom의 key는 앱 전체에서 고유해야 합니다.
export const cartState = atom({
  key: 'cartState', // 고유 ID
  default: null, // 기본값 (처음엔 장바구니 정보가 없음)
});