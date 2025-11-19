import { useState, useEffect } from 'react';
import api from '../api/axiosConfig';
import { Link } from 'react-router-dom';

// MUI 컴포넌트
import { 
    Typography, 
    Box, 
    CircularProgress, 
    Paper, 
    Divider, 
    Grid,
    Button 
} from '@mui/material';

function OrderHistoryPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  // 1. (실행) 컴포넌트 로드 시 '내 주문 내역' API 호출
  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        // (★핵심★) Axios로 'GET /api/orders/my' 호출
        // (인터셉터가 자동으로 토큰을 헤더에 추가)
        const response = await api.get('/api/orders/my');
        
        // 34단계에서 만든 OrderResponseDto[] 형태의 데이터
        setOrders(response.data);

      } catch (error) {
        console.error('주문 내역 조회 실패:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []); // [] : 처음 한 번만 실행

  // 2. (그리기) 로딩 중
  if (loading) {
    return (
      <Box className="flex justify-center items-center h-64">
        <CircularProgress />
        <Typography className="ml-4">주문 내역을 불러오는 중...</Typography>
      </Box>
    );
  }

  // 3. (그리기) 주문 내역이 없을 때
  if (orders.length === 0) {
    return (
      <Paper className="p-6 text-center max-w-lg mx-auto">
        <Typography variant="h6" component="h2" className="mb-4">
          📜 내 주문 내역
        </Typography>
        <Typography color="textSecondary">
          아직 주문한 내역이 없습니다.
        </Typography>
      </Paper>
    );
  }

  // 4. (그리기) 주문 내역이 있을 때
  return (
    <Box className="max-w-4xl mx-auto p-4">
      <Typography variant="h4" component="h1" className="text-center font-bold mb-8">
        📜 내 주문 내역
      </Typography>
      
      {/* 주문 목록 (최신순) */}
      <Box className="space-y-6">
        {orders.map(order => (
          <Paper key={order.orderId} elevation={3} className="p-6 rounded-lg">
            
            {/* 주문 헤더 (주문 ID, 날짜, 상태) */}
            <Box className="flex justify-between items-center mb-4 border-b pb-3">
              <div>
                <Typography variant="h6" className="font-semibold">
                  주문 ID: {order.orderId}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  {/* 날짜 포맷팅 (간단하게) */}
                  주문 일시: {new Date(order.orderDate).toLocaleString('ko-KR')}
                </Typography>
              </div>
              <Typography 
                variant="h6"
                // (Tailwind) 주문 상태에 따라 색상 변경
                className={`font-bold ${order.orderStatus === 'CANCEL' ? 'text-red-500' : 'text-green-600'}`}
              >
                {order.orderStatus === 'ORDER' ? '주문 완료' : '주문 취소'}
              </Typography>
            </Box>

            {/* 주문 상품 목록 */}
            <Box className="space-y-4">
              {order.orderItems.map(item => (
                <Grid container key={item.productId} spacing={2} className="items-center">
                  <Grid xs={8}>
                    <Typography variant="subtitle1" className="font-medium">{item.productName}</Typography>
                    <Typography variant="body2" color="textSecondary">
                      {item.orderPrice.toLocaleString()}원 x {item.count}개
                    </Typography>
                  </Grid>
                  <Grid xs={4} className="text-right">
                    <Typography variant="subtitle1" className="font-bold">
                      {(item.orderPrice * item.count).toLocaleString()}원
                    </Typography>
                  </Grid>
                </Grid>
              ))}
            </Box>
            
            {/* (참고) 주문 취소 버튼은 여기서 처리할 수도 있음 */}
            {order.orderStatus === 'ORDER' && (
              <Button 
                variant="outlined" 
                color="error" 
                size="small"
                className="mt-4"
                // onClick={() => handleCancelOrder(order.orderId)} // (주문 취소 로직 연결)
              >
                주문 취소하기
              </Button>
            )}

          </Paper>
        ))}
      </Box>
    </Box>
  );
}

export default OrderHistoryPage;