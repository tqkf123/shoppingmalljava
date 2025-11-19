import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import { useSetRecoilState } from 'recoil';
import { cartState } from '../store/cartState';

// MUI 컴포넌트
import { Button, Typography, Box, CircularProgress, Paper, Grid } from '@mui/material';

function ProductDetailPage() {
  // 1. URL 파라미터에서 productId 가져오기
  const { productId } = useParams(); 
  
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const setGlobalCart = useSetRecoilState(cartState);
  const navigate = useNavigate(); // 페이지 이동용

  // 2. productId로 단일 상품 API 호출
  useEffect(() => {
    const fetchProduct = async () => {
      // productId가 유효하지 않으면 API 호출 방지
      if (!productId) {
          setLoading(false);
          navigate('/'); // 홈으로
          return;
      }
      
      try {
        setLoading(true);
        const response = await api.get(`/api/products/${productId}`);
        setProduct(response.data);
      } catch (error) {
        console.error('상품 상세 정보 로딩 실패:', error);
        alert('상품을 찾을 수 없습니다.');
        navigate('/'); // 에러 발생 시 홈으로 이동
      } finally {
        setLoading(false);
      }
    };
    
    fetchProduct();
  }, [productId, navigate]); // 의존성 배열에 navigate 추가

  // 3. 장바구니 담기 핸들러
  const handleAddToCart = async () => {
    if (!product) return;
    
    const requestDto = { productId: product.id, count: 1 }; // (수량 1개 고정)
    try {
      const response = await api.post('/api/cart', requestDto);
      setGlobalCart(response.data);
      alert('장바구니에 상품을 담았습니다!');
    } catch (error) {
      if (error.response && error.response.status === 401) {
        alert('로그인이 필요합니다.');
        navigate('/auth'); // 로그인 페이지로 이동
      } else {
        alert('장바구니 추가 실패: ' + (error.response?.data || error.message));
      }
    }
  };

  // 4. (그리기) 로딩 중
  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <CircularProgress />
      </div>
    );
  }

  // 5. (그리기) 상품 없음
  if (!product) {
    return <Typography variant="h5">상품을 찾을 수 없습니다.</Typography>;
  }

  // 6. (그리기) 상품 상세 정보
  return (
    <Paper elevation={4} className="max-w-4xl mx-auto p-8">
      <Grid container spacing={4}>
        {/* --- [ 상품 이미지 영역 ] --- */}
        <Grid xs={12} md={6}>
          {product.imageUrl ? (
              <img 
                  src={`http://localhost:8080${product.imageUrl}`} 
                  alt={product.name} 
                  className="w-full h-96 object-contain rounded-lg shadow-md"
              />
          ) : (
              <Box
                  className="w-full h-96 bg-gray-100 flex items-center justify-center rounded-lg shadow-md"
              >
                  <Typography color="textSecondary" variant="h6">No Image</Typography>
              </Box>
          )}
        </Grid>
        {/* ------------------------------------ */}
        
        {/* 상품 정보 영역 */}
        <Grid xs={12} md={6}>
          <Typography variant="h4" component="h1" className="font-bold mb-4">
            {product.name}
          </Typography>
          <Typography variant="body1" color="textSecondary" className="mb-6">
            {product.description}
          </Typography>
          <Typography variant="h3" color="primary" className="font-bold mb-4">
            {product.price.toLocaleString()}원
          </Typography>
          <Typography variant="body1" className="text-gray-600 mb-6">
            재고: {product.stockQuantity}개
          </Typography>
          
          <Button 
            variant="contained" 
            color="success" 
            size="large"
            fullWidth
            onClick={handleAddToCart}
          >
            장바구니 담기
          </Button>
        </Grid>
      </Grid>
    </Paper>
  );
}

export default ProductDetailPage;