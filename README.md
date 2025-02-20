# 📜 Contents
- [1️⃣ 프로젝트 개요](#1️⃣-프로젝트-개요)
- [2️⃣ 주요 기능](#2️⃣-주요-기능)
- [3️⃣ 개발 환경](#3️⃣-개발-환경)
- [4️⃣ 기술 특이점](#4️⃣-기술-특이점)
- [5️⃣ 산출물](#5️⃣-산출물)
- [6️⃣ 팀원 소개](#6️⃣-팀원-소개)
- [7️⃣ 실제 화면](#7️⃣-실제-화면)

---

# 1️⃣ 프로젝트 개요

### **프로젝트명**: **STEACH** (*Study & Teach*)  
**서비스 특징**: 교육 기회가 부족한 분들에게 학습 기회를 제공하여 교육 격차 해소를 목표로 하는 무료 온라인 교육 서비스  

---

# 2️⃣ 주요 기능
- **화상 강의실** (WebRTC 기반)
- **생성형 AI 기반 진로 추천**
- **AI 기반 졸음 감지 및 경고 시스템**

---

# 3️⃣ 개발 환경

### **🔹 주요 기술**
- **WebRTC** (실시간 화상 강의)
- **WebSocket** (실시간 데이터 통신)
- **JWT Authentication** (보안 및 인증)
- **AI 기술 활용** (진로 추천, 졸음 감지)
- **테스트 자동화** (JUnit5, Mockito, RestAssured)
- **REST API** (백엔드-프론트엔드 통신)
- 
### **🔹 Frontend**
- **React**
- **JavaScript & TypeScript**
- **Redux + Redux Toolkit**
- **Tailwind CSS**

### **🔹 Backend**
- **Java 17**
- **Spring Boot 3.2.7** (Gradle 6.9.3)
- **WebRTC** (실시간 강의)
- **JPA / MariaDB**
- **MongoDB** (진로 추천 데이터 수집 및 분석)
- **QueryDSL** (복잡한 쿼리 최적화 및 성능 개선)
- **JUnit5 + Mockito + RestAssured** (테스트)

### **🔹 배포 환경**
- **AWS**
- **Docker**
- **Nginx**
- **CI/CD (Jenkins)**

---

# 4️⃣ 기술 특이점
### ✅ **보안 강화**
- JWT 기반 인증 시스템 + Refresh Token 적용  
- 역할 기반 접근 제어 (**RBAC**)  
- CORS 및 CSRF 보안 정책 적용

### ✅ **MongoDB 기반 AI 추천 시스템**
- **학생의 학습 패턴 및 퀴즈 데이터를 MongoDB에 저장하여 AI 분석**  
- **수업 참여율, 퀴즈 정답률 등 데이터를 활용한 개인 맞춤형 진로 추천**  
- **비정형 데이터 분석을 위한 NoSQL 도입 및 최적화**  

### ✅ **테스트 전략**
- **단위 테스트 (Unit Test)**: JUnit5, Mockito 활용  
- **통합 테스트 (Integration Test)**: Testcontainers, Spring Boot Test  
- **E2E 테스트 (End-to-End Test)**: RestAssured  
- **CRUD 테스트가 아니라 주요 기능 흐름을 중심으로 인수 테스트 진행**  

### ✅ **커스텀 예외 처리**
- **핵심 예외 클래스 정의 및 중앙 관리 (`GlobalControllerAdvice`)**  
- **사용자 권한 오류, 데이터 중복, 리소스 미존재 등의 상황별 예외 처리**  
- **일관된 에러 응답 포맷 유지**  


### ✅ **QueryDSL을 활용한 성능 최적화**
- **JPA의 복잡한 조회 성능을 향상시키기 위해 QueryDSL 도입**
- **불필요한 데이터 조회 방지 및 고성능 쿼리 적용**
- **학생 강의실 조회 및 세션 ID 검색 최적화**  

### ✅ **로그 분석 및 모니터링**
- **Grafana + Loki**를 활용한 **실시간 로그 분석 및 시각화**  
- **애플리케이션 성능 모니터링을 통한 문제 진단 및 대응**  


---

# 5️⃣ 산출물

### **📌 시스템 아키텍처**
![아키택처](https://github.com/user-attachments/assets/abcf5244-66ea-4962-b857-067a6af00c7e)
**[시스템 구성도 보기](https://lab.ssafy.com/s11-webmobile1-sub2/S11P12D201/-/blob/master/%EC%82%B0%EC%B6%9C%EB%AC%BC/sub2/%EC%84%A4%EA%B3%84/%EC%8B%9C%EC%8A%A4%ED%85%9C%EA%B5%AC%EC%84%B1%EB%8F%84.md?ref_type=heads)**


### **📌 데이터베이스 설계 (ERD)**
![ERD](https://github.com/user-attachments/assets/da90f0c3-7702-4362-9452-b2c673f6b117)
**[ERD 보기](https://lab.ssafy.com/s11-webmobile1-sub2/S11P12D201/-/blob/master/%EC%82%B0%EC%B6%9C%EB%AC%BC/sub2/%EC%84%A4%EA%B3%84/ERD.md?ref_type=heads)**

### **📌 UI & MockUp**
📌 **[Figma 디자인 보기](https://www.figma.com/design/Ty9shKBP9wq01ayhCGvFd6/%EC%8A%A4%ED%8B%B0%EC%B9%98_%EC%9E%91%EC%84%B1%EC%A4%91?node-id=0-1&t=LIbx5xSG07wwQM8P-0)**

### **📌 기능 명세서**
📌 **[기능 명세서 보기](https://lab.ssafy.com/s11-webmobile1-sub2/S11P12D201/-/blob/master/%EC%82%B0%EC%B6%9C%EB%AC%BC/sub2/%EC%84%A4%EA%B3%84/%EA%B8%B0%EB%8A%A5%EB%AA%85%EC%84%B8%EC%84%9C.md?ref_type=heads)**

---

# 6️⃣ 팀원 소개
| 이름 | 역할 |
|------|------|
| **조시현** | 팀장, PM, QA, 백엔드, 인프라 |
| **이진송** | 프론트엔드 테크리더 |
| **이상철** | 백엔드 테크리더, 서버 보안 관리 |
| **김호경** | 데이터 테크리더, 백엔드 |
| **주효림** | 백엔드, 백엔드 테스트 리더 |
| **감헌규** | 프론트엔드, 프론트 테스트 리더 |




# 7️⃣ 실제 화면
## 🔹 메인 페이지
<img width="893" alt="image" src="https://github.com/user-attachments/assets/3f440e90-2801-4ad9-a575-b43ea2404404" />

## 🔹 화상 강의 화면
![강의](https://github.com/user-attachments/assets/bf3f59f4-176a-416c-a9b3-89ea880cf079)

## 🔹 AI 진로 추천 결과 및 통계 화면
![steach통계](https://github.com/user-attachments/assets/abffddd6-29f8-4745-92ec-ad6763718f23)
