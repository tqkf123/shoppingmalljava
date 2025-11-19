import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom'
import { RecoilRoot } from 'recoil' // [1. ★추가★]
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      {/* [2. ★추가★] RecoilRoot로 App을 감쌉니다 */}
      <RecoilRoot> 
        <App />
      </RecoilRoot>
    </BrowserRouter>
  </React.StrictMode>,
)