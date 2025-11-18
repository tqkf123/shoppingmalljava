# 🛍️ Spring Boot + React 쇼핑몰 프로젝트 (MyShop)

## 📖 프로젝트 소개
사용자에게 직관적인 쇼핑 경험을 제공하고, 관리자에게는 효율적인 상품 관리 기능을 제공하는 **풀스택 쇼핑몰 서비스**입니다.
**트랜잭션 처리, N+1 문제 해결, 보안(JWT)** 등 백엔드의 핵심 기술적 챌린지를 해결하는 데 집중했습니다.

## 🎥 시연 영상
(여기에 유튜브 링크나 GIF 이미지를 넣으세요!)
![시연영상](https://your-image-link.com/demo.gif)

## 🛠️ 기술 스택 (Tech Stack)
* **Backend:** Java 17, Spring Boot 3.2, Spring Security, JPA, QueryDSL, MySQL
* **Frontend:** React, Vite, Recoil, Axios, Tailwind CSS, MUI
* **Payment:** PortOne (KakaoPay)


## 📂 프로젝트 구조 (Project Structure)

### Backend (Spring Boot)
핵심 비즈니스 로직을 도메인별로 분리하고, QueryDSL과 Security 설정을 모듈화하여 관리했습니다.

src/main/java/com/example/shoppingmall
├── config            # 설정 파일 (Security, CORS, QueryDSL, JWT)
│   ├── JwtAuthenticationFilter.java
│   ├── SecurityConfig.java
│   └── WebConfig.java
├── controller        # API 엔드포인트 (요청/응답 처리)
├── domain            # JPA Entity (DB 테이블 매핑)
│   ├── product
│   ├── user
│   └── ...
├── dto               # Data Transfer Object (데이터 전송 객체)
├── repository        # DAO 계층 (Spring Data JPA + QueryDSL)
│   ├── ProductRepositoryImpl.java (QueryDSL 구현)
│   └── ...
└── service           # 비즈니스 로직 & 트랜잭션 처리


src
├── api               # Axios 설정 (Interceptor, BaseURL)
│   └── axiosConfig.js
├── components        # 재사용 가능한 UI 컴포넌트
│   ├── Cart.jsx
│   ├── Login.jsx
│   └── SignUp.jsx
├── pages             # 라우터 페이지 (View)
│   ├── AdminProductPage.jsx
│   ├── HomePage.jsx
│   ├── OrderHistoryPage.jsx
│   └── ...
├── store             # Recoil 전역 상태 관리 (Atom)
│   ├── authState.js
│   └── cartState.js
├── App.jsx           # 라우팅 및 레이아웃 설정
└── main.jsx          # 진입점 (Provider 설정)


## 🔥 핵심 기능 및 문제 해결 (Key Features)

### 1. 안전하고 유연한 인증 시스템 (Security + JWT)
* **문제:** 세션 기반 인증은 서버 확장에 불리함.
* **해결:** **JWT(Json Web Token)**을 도입하여 Stateless한 인증 구현.
* **디테일:** `Axios Interceptor`를 구현하여 프론트엔드에서 토큰을 자동으로 헤더에 주입.
* **보안:** 관리자(ADMIN)와 일반 사용자(USER)의 **권한(Role)을 분리**하여 API 접근 제어.

### 2. 대용량 데이터 처리를 위한 검색 최적화 (QueryDSL)
* **문제:** `findAll()` 사용 시 데이터가 많아지면 성능 저하 발생.
* **해결:** **QueryDSL**을 도입하여 동적 쿼리 및 **페이징(Pagination)** 구현.
* **성과:** 필요한 데이터만 조회하여 응답 속도 및 DB 부하 최소화.

### 3. 데이터 무결성을 보장하는 주문 시스템
* **트랜잭션:** 주문 생성 시 `재고 차감 - 주문 저장 - 상세 저장` 과정을 `@Transactional`로 묶어, 중간에 실패 시 **전체 롤백(Rollback)** 처리.
* **동시성 고려:** 재고가 0 미만이 되면 `CustomException`을 발생시켜 주문 차단.

### 4. 사용자 경험(UX) 최적화
* **실시간 상태 관리:** **Recoil**을 사용하여 장바구니 담기 즉시 네비게이션 바 숫자 업데이트.
* **스켈레톤 UI:** 데이터 로딩 중 뼈대 UI를 보여주어 이탈률 감소 유도.
* **결제 연동:** **PortOne API**를 연동하여 실제 결제 프로세스 구현.

## ⚙️ 실행 방법 (How to run)
1.  MySQL에 `shoppingmall_db` 스키마 생성
2.  `git clone ...`
3.  Backend: `application.properties` DB 정보 수정 후 실행
4.  Frontend: `npm install` -> `npm run dev`****
