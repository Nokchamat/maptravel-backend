# MapTravel
여행에 필요한 정보를 볼 수 있고 공유하는 앱

## 기술 스택
Backend : Java, Spring Boot, JPA, Docker, Mysql \
DevOps : AWS EC2, AWS RDS, AWS S3, Docker \
Front : Flutter

## 개발 정보
- 개발 인원 : 1명 (본인)
- Backend 개발 시작 : 2023/11/12 ~ (진행 중)

## ERD
<img width="1217" alt="image" src="https://github.com/Nokchamat/maptravel-backend/assets/107979129/f1e3f189-a3a8-4ee1-aeae-e1235d992cab">


  

## 프로젝트 주요 기능
### 회원가입 / 로그인
    회원가입
    - OAuth2로 예정
        - 카카오 로그인
        - 구글 로그인
        - 애플 로그인

    로그인
    - Oauth2로 예정

### 게시물
    작성
    - 로그인 상태에서 작성 가능

    조회
    - 게시물 조회 시에는 로그인 없이 가능
    - 상세 조회 시 조회수 증가

### 좋아요
    - 게시물에 대해 좋아요 등록, 취소, 조회

### 팔로우
    - 사람에 대해 팔로우 등록, 취소, 조회

### 채팅
    - 게시물 작성자와 채팅 가능
