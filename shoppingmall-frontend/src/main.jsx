import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom' // [1. ★추가★]
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    {/* ★2. <BrowserRouter>로 <App />이 감싸져 있는지 확인 */}
    <BrowserRouter> 
      <App />
    </BrowserRouter>
  </React.StrictMode>,
)