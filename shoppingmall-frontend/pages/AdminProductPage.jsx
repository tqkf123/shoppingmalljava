import { useState } from 'react';
import api from '../api/axiosConfig';
import toast from 'react-hot-toast';

// MUI
import { Button, Grid, TextField, Box, Typography, Paper } from '@mui/material';

function AdminProductPage() {
  // (HomePage.jsx에서 가져온 상품 등록 폼 상태)
  const [newProduct, setNewProduct] = useState({
    name: '',
    description: '',
    price: '',
    stockQuantity: ''
  });
  const [imageFile, setImageFile] = useState(null);

  // (HomePage.jsx에서 가져온 핸들러 함수들)
  const handleNewProductChange = (e) => {
    const { name, value } = e.target;
    setNewProduct(prev => ({ ...prev, [name]: value }));
  };

  const handleImageFileChange = (e) => {
    if (e.target.files && e.target.files.length > 0) {
      setImageFile(e.target.files[0]);
    } else {
      setImageFile(null);
    }
  };

  const handleAddProduct = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('productDto', new Blob([JSON.stringify(newProduct)], {
      type: "application/json"
    }));
    if (imageFile) {
      formData.append('image', imageFile);
    }

    try {
      // (★보안★) 백엔드 SecurityConfig가 이 요청을 'ADMIN'만 허용
      const response = await api.post('/api/products', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      
      toast.success('상품이 성공적으로 등록되었습니다.');
      
      // 폼 초기화
      setNewProduct({ name: '', description: '', price: '', stockQuantity: '' });
      setImageFile(null);
      if(document.getElementById('image-upload-input-admin')) {
        document.getElementById('image-upload-input-admin').value = '';
      }
    } catch (error) {
      // (★보안★) ADMIN이 아닌 유저가 시도하면 403 Forbidden 에러 발생
      if (error.response && (error.response.status === 401 || error.response.status === 403)) {
         toast.error('상품 등록 권한이 없습니다.');
      } else {
         toast.error('상품 등록 실패: ' + (error.response?.data || error.message));
      }
    }
  };

  return (
    <Paper elevation={4} className="max-w-2xl mx-auto p-8">
      <Typography variant="h4" component="h1" className="text-center font-bold mb-8">
        📦 상품 관리 (Admin)
      </Typography>
      
      <Box component="form" onSubmit={handleAddProduct} className="p-6 border rounded-lg">
        <Typography variant="h5" component="h3" className="mb-4">새 상품 등록</Typography>
        <Grid container spacing={2}>
          <Grid xs={12} sm={6}>
            <TextField label="상품명" name="name" fullWidth value={newProduct.name} onChange={handleNewProductChange} required size="small" />
          </Grid>
          <Grid xs={12} sm={6}>
            <TextField label="가격" name="price" type="number" fullWidth value={newProduct.price} onChange={handleNewProductChange} required size="small" />
          </Grid>
          <Grid xs={12}>
            <TextField label="설명" name="description" fullWidth multiline rows={2} value={newProduct.description} onChange={handleNewProductChange} required />
          </Grid>
          <Grid xs={12} sm={6}>
            <TextField label="재고 수량" name="stockQuantity" type="number" fullWidth value={newProduct.stockQuantity} onChange={handleNewProductChange} required size="small" />
          </Grid>
          <Grid xs={12} sm={6} className="flex items-center">
            <input type="file" id="image-upload-input-admin" accept="image/*" onChange={handleImageFileChange} className="mt-2" />
          </Grid>
          <Grid xs={12}>
            <Button type="submit" variant="contained" color="primary" fullWidth className="mt-2">상품 등록</Button>
          </Grid>
        </Grid>
      </Box>
      {/* (참고) 여기에 '상품 수정/삭제' 목록을 추가하면 완벽한 관리자 페이지가 됨 */}
    </Paper>
  );
}

export default AdminProductPage;