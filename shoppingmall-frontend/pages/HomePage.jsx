import { useState, useEffect } from 'react';
import api from '../api/axiosConfig';
import { useSetRecoilState } from 'recoil';
import { cartState } from '../store/cartState';
import { Link } from 'react-router-dom';
import toast from 'react-hot-toast';

// MUI 컴포넌트
import { 
    Button, 
    Grid, 
    Card, 
    CardContent, 
    Typography, 
    Box,
    Skeleton,
    Pagination,
    TextField,
    InputAdornment,
    IconButton
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

function HomePage() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const setGlobalCart = useSetRecoilState(cartState);

  // --- [ 페이징 & 검색 상태 ] ---
  const [page, setPage] = useState(1); // 현재 페이지
  const [totalPages, setTotalPages] = useState(1); // 전체 페이지 수
  const [keyword, setKeyword] = useState(''); // 검색어 입력값
  const [searchTrigger, setSearchTrigger] = useState(''); // 실제 검색 실행 키워드

  // --- [ 1. 상품 목록 불러오기 ] ---
  useEffect(() => {
    fetchProducts(page, searchTrigger);
  }, [page, searchTrigger]);

  const fetchProducts = async (pageNumber, searchKeyword) => {
    try {
      setLoading(true);
      
      // 백엔드 API 호출 (페이징 + 검색)
      const response = await api.get(`/api/products`, {
        params: {
          page: pageNumber - 1, // 백엔드는 0부터 시작
          size: 8, // 한 페이지당 8개
          keyword: searchKeyword
        }
      });

      setProducts(response.data.content); 
      setTotalPages(response.data.totalPages);
      
    } catch (error) {
      console.error('상품 목록 로딩 실패:', error);
      toast.error('상품 목록을 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // --- [ 검색 핸들러 ] ---
  const handleSearch = (e) => {
    e.preventDefault();
    setPage(1); // 검색 시 1페이지로 초기화
    setSearchTrigger(keyword);
  };

  // --- [ 페이지 변경 핸들러 ] ---
  const handlePageChange = (event, value) => {
    setPage(value);
  };

  // --- [ 장바구니 담기 핸들러 ] ---
  const handleAddToCart = async (productId) => {
    const requestDto = { productId: productId, count: 1 };
    try {
      const response = await api.post('/api/cart', requestDto);
      setGlobalCart(response.data); 
      toast.success('장바구니에 상품을 담았습니다!');
    } catch (error) {
      if (error.response && error.response.status === 401) {
        toast.error('로그인이 필요합니다.');
      } else {
        toast.error('장바구니 추가 실패: ' + (error.response?.data || error.message));
      }
    }
  };

  // --- [ 로딩 중 스켈레톤 UI ] ---
  if (loading) {
    return (
      <div className="container mx-auto p-4">
        {/* 검색바 스켈레톤 대신 헤더 표시 */}
        <Box className="flex justify-between items-center mb-6">
             <h2 className="text-2xl font-semibold">🛒 상품 목록 (불러오는 중...)</h2>
        </Box>

        <Grid container spacing={3}>
          {Array.from(new Array(8)).map((item, index) => (
            <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3 }} key={index}>
              <Card className="shadow-lg h-full flex flex-col">
                <Skeleton variant="rectangular" width="100%" height={192} />
                <CardContent className="flex-grow">
                  <Skeleton variant="text" height={32} width="80%" />
                  <Skeleton variant="text" height={20} />
                  <Skeleton variant="text" height={20} width="60%" />
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </div>
    );
  }

  // --- [ 실제 UI 렌더링 ] ---
  return (
    <div className="container mx-auto p-4">
      
      {/* 검색 바 */}
      <Box component="form" onSubmit={handleSearch} className="flex flex-col sm:flex-row justify-between items-center mb-6 gap-4">
        <h2 className="text-2xl font-semibold">🛒 상품 목록</h2>
        <Box className="flex gap-2 w-full sm:w-auto">
            <TextField 
                size="small"
                placeholder="상품 검색..."
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                className="w-full sm:w-64"
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            <IconButton onClick={handleSearch}>
                                <SearchIcon />
                            </IconButton>
                        </InputAdornment>
                    )
                }}
            />
        </Box>
      </Box>
      
      {/* 상품 목록 Grid */}
      <Grid container spacing={3}>
        {products.length > 0 ? (
            products.map(product => (
            <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3 }} key={product.id}>
                
                <Link to={`/products/${product.id}`} className="no-underline"> 
                <Card className="shadow-lg hover:shadow-xl transition-shadow duration-300 h-full flex flex-col">
                    
                    {/* 이미지 표시 */}
                    {product.imageUrl ? (
                        <img 
                            src={`http://localhost:8080${product.imageUrl}`} 
                            alt={product.name} 
                            className="w-full h-48 object-cover rounded-t-lg"
                        />
                    ) : (
                        <Box className="w-full h-48 bg-gray-100 flex items-center justify-center rounded-t-lg">
                            <Typography color="textSecondary">No Image</Typography>
                        </Box>
                    )}

                    <CardContent className="flex-grow">
                    <Typography gutterBottom variant="h6" component="div" className="truncate">
                        {product.name}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" className="mb-2 h-10 overflow-hidden text-ellipsis">
                        {product.description}
                    </Typography>
                    <Typography variant="h5" color="primary" className="font-bold mb-3">
                        {product.price.toLocaleString()}원
                    </Typography>
                    <div className="text-sm text-gray-500 mb-4">
                        재고: {product.stockQuantity}개
                    </div>
                    </CardContent>
                    
                    <div className="p-4 pt-0">
                    <Button 
                        variant="contained" 
                        color="success" 
                        fullWidth
                        onClick={(e) => {
                        e.preventDefault(); // Link 이동 방지
                        handleAddToCart(product.id);
                        }}
                    >
                        장바구니 담기
                    </Button>
                    </div>
                </Card>
                </Link> 
            </Grid>
            ))
        ) : (
            <Box className="w-full text-center py-20">
                <Typography variant="h6" color="textSecondary">검색 결과가 없습니다.</Typography>
            </Box>
        )}
      </Grid>

      {/* 페이지네이션 */}
      {products.length > 0 && (
        <Box className="flex justify-center mt-10 mb-8">
            <Pagination 
                count={totalPages} 
                page={page} 
                onChange={handlePageChange} 
                color="primary" 
                size="large"
            />
        </Box>
      )}

    </div>
  );
}

export default HomePage;