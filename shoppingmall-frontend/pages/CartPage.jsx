import Cart from '../components/Cart'; // 1. 위에서 만든 Cart 컴포넌트 import

function CartPage() {
    return (
        // Tailwind CSS로 페이지 레이아웃 설정
        <div className="max-w-3xl mx-auto p-4"> 
            {/* max-w-3xl : 최대 너비 제한 (너무 넓어지지 않게)
                mx-auto : 좌우 마진 자동 (가운데 정렬)
                p-4 : 패딩
            */}
            
            {/* (참고) <Cart /> 컴포넌트 자체가 Paper(배경/그림자)를
              가지고 있으므로 여기서는 추가 스타일링이 필요 없습니다.
            */}
            <Cart />
        </div>
    );
}

export default CartPage;