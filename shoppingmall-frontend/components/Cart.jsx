import { useEffect } from 'react'; // [★ 1. useEffect import 추가]
import { useRecoilState } from 'recoil';
import { cartState } from '../store/cartState';
import api from '../api/axiosConfig';
import toast from 'react-hot-toast';

import { Button, Typography, Box, Paper, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';

function Cart() {
  const [cart, setCart] = useRecoilState(cartState);

  // --- [ ★ 2. 추가 ★: 페이지 로드 시 장바구니 최신화 ] ---
  useEffect(() => {
    const fetchCart = async () => {
      try {
        const response = await api.get('/api/cart');
        setCart(response.data);
      } catch (error) {
        console.error("장바구니 조회 실패:", error);
        // (로그인 안 된 상태면 401 등이 뜰 수 있음)
      }
    };
    fetchCart();
  }, [setCart]); 
  // -----------------------------------------------------

  // --- [ 아이템 삭제 핸들러 ] ---
  const handleDeleteItem = async (cartItemId) => {
    if (!cart) return;
    try {
      const response = await api.delete(`/api/cart/items/${cartItemId}`);
      setCart(response.data);
      toast.success('상품이 장바구니에서 삭제되었습니다.');
    } catch (error) {
      toast.error('삭제에 실패했습니다: ' + (error.response?.data?.message || error.message));
    }
  };

  // --- [ 수량 변경 핸들러 ] ---
  const handleUpdateCount = async (cartItemId, newCount) => {
    if (!cart) return;
    
    if (newCount <= 0) {
      handleDeleteItem(cartItemId);
      return;
    }

    try {
      const response = await api.patch(`/api/cart/items/${cartItemId}`, {
        count: newCount
      });
      setCart(response.data);
    } catch (error) {
      toast.error(error.response?.data || '수량 변경 실패');
    }
  };

  // --- [ 주문하기 핸들러 (결제 연동) ] ---
  const handleOrder = async () => {
    if (!cart || cart.cartItems.length === 0) {
      toast.error('장바구니가 비어있습니다.');
      return;
    }

    const totalPrice = cart.cartItems.reduce((total, item) => {
      return total + (item.price * item.count);
    }, 0);

    if (!window.IMP) {
        toast.error("결제 모듈을 불러오지 못했습니다. 새로고침 해주세요.");
        return;
    }
    const { IMP } = window;
    
    // 본인의 가맹점 식별코드로 변경 (테스트용: imp91558307)
    IMP.init('imp04660056'); 

    const paymentData = {
      pg: 'kakaopay',
      pay_method: 'card',
      merchant_uid: `mid_${new Date().getTime()}`,
      name: 'MyShop 상품 결제',
      amount: totalPrice,
      buyer_email: 'test@example.com',
      buyer_name: '홍길동',
    };

    IMP.request_pay(paymentData, async (rsp) => {
      if (rsp.success) {
        try {
          const orderRequestDto = {
            orderItems: cart.cartItems.map(item => ({
              productId: item.productId,
              count: item.count
            }))
          };
          
          const response = await api.post('/api/orders', orderRequestDto);
          
          toast.success(`결제 성공! 주문 ID: ${response.data.split(': ')[1]}`);
          
          const updatedCartResponse = await api.get('/api/cart');
          setCart(updatedCartResponse.data);

        } catch (error) {
          console.error('주문 생성 실패:', error);
          toast.error('결제는 성공했으나 주문 생성에 실패했습니다.');
        }
      } else {
        toast.error(`결제 실패: ${rsp.error_msg}`);
      }
    });
  };


  // --- [ 화면 렌더링 ] ---
  
  // 1. 장바구니 비었을 때
  if (!cart || cart.cartItems.length === 0) {
    return (
      <Paper elevation={3} className="p-6">
        <Typography variant="h6" component="h2" className="mb-4">
          🛒 내 장바구니
        </Typography>
        <Typography color="textSecondary">
          장바구니가 비어있습니다.
        </Typography>
      </Paper>
    );
  }

  // 2. 장바구니 내용 있을 때
  const totalPrice = cart.cartItems.reduce((total, item) => {
    return total + (item.price * item.count);
  }, 0);

  return (
    <Paper elevation={3} className="p-6">
      <Typography variant="h6" component="h2" className="mb-4">
        🛒 내 장바구니
      </Typography>
      
      <Box className="space-y-4 mb-6">
        {cart.cartItems.map(item => (
          <Box 
            key={item.cartItemId} 
            className="flex flex-col sm:flex-row justify-between sm:items-center border-b pb-3"
          >
            <Box className="flex-grow mb-2 sm:mb-0">
              <Typography variant="subtitle1" className="font-medium">{item.productName}</Typography>
              <Typography variant="body2" color="textSecondary">
                {item.price.toLocaleString()}원
              </Typography>
            </Box>

            <Box className="flex justify-between items-center w-full sm:w-auto">
              <Box className="flex items-center">
                <IconButton 
                  size="small" 
                  color="primary"
                  onClick={() => handleUpdateCount(item.cartItemId, item.count - 1)}
                >
                  <RemoveIcon fontSize="small" />
                </IconButton>
                <Typography className="mx-2 font-bold w-8 text-center">{item.count}</Typography>
                <IconButton 
                  size="small" 
                  color="primary"
                  onClick={() => handleUpdateCount(item.cartItemId, item.count + 1)}
                >
                  <AddIcon fontSize="small" />
                </IconButton>
              </Box>

              <Typography variant="subtitle1" className="font-bold w-24 text-right mx-4">
                {(item.price * item.count).toLocaleString()}원
              </Typography>

              <IconButton 
                color="error"
                size="small"
                onClick={() => handleDeleteItem(item.cartItemId)}
              >
                <DeleteIcon fontSize="small" />
              </IconButton>
            </Box>
          </Box>
        ))}
      </Box>
      
      <Box className="flex justify-between items-center mt-6 pt-4 border-t">
          <Typography variant="h6" className="font-bold">총액:</Typography>
          <Typography variant="h5" color="primary" className="font-bold">
              {totalPrice.toLocaleString()}원
          </Typography>
      </Box>

      <Button 
        variant="contained" 
        color="warning" 
        fullWidth 
        className="mt-6 font-bold text-lg py-3"
        onClick={handleOrder}
      >
        {totalPrice.toLocaleString()}원 결제하기
      </Button>
    </Paper>
  );
}

export default Cart;