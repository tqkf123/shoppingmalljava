import Login from '../components/Login';
import SignUp from '../components/SignUp';

// MUI 컴포넌트 import
import { Grid, Paper, Typography, Box } from '@mui/material';

function AuthPage() {
  return (
    <Box className="container mx-auto p-4 max-w-4xl">
      <Paper elevation={4} className="p-8">
        
        <Typography variant="h4" component="h1" className="text-center mb-8 font-bold">
          🛍️ MyShop 로그인
        </Typography>

        {/* --- [ ★ 1. 수정 ★: MUI Grid v5 문법 적용 ] --- */}
        <Grid container spacing={5} justifyContent="center" alignItems="flex-start">
          
          {/* [수정] <Grid item xs={12} md={6}> -> <Grid xs={12} md={6}> */}
          <Grid xs={12} md={6}>
            <Login />
          </Grid>
          
          {/* [수정] <Grid item xs={12} md={6}> -> <Grid xs={12} md={6}> */}
          <Grid xs={12} md={6}>
            <SignUp />
          </Grid>

        </Grid>
      </Paper>
    </Box>
  );
}

export default AuthPage;