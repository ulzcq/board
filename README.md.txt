# 스프링 게시판 프로젝트 🍃

> 프로젝트 참여 인원 : 1명 (개인 프로젝트)

## 사용 기술

**Server-side**

- JAVA 8
- Spring Boot 2.7.2
- Spring Data JPA
- Querydsl 1.0.10
- JUnit 5
- H2-DB
- Thymeleaf
- Bootstrap

## 구현 기능

- 세션-쿠키 기반의 로그인 / 회원가입 구현
- 기본적인 게시글 CRUD가 가능한 계층 게시판 홈페이지
- 첨부파일 업로드
- 게시판 검색
- 회원 정보 / 비밀번호 변경

## 추후 추가할 기능

- 내가 작성한 게시글 목록 조회
- 댓글, 대댓글 작성 및 삭제 (대댓글 까지만 허용하는 계층구조)
- 내가 댓글 단 게시글 목록 조회

- Security & JWT를 이용한 Authentication
- 회원 / 관리자 Role 부여
- Redis를 이용한 로그인 정보 Cache
- AOP를 사용하여 로그 기록 남기기

- 게시글 추천
- 특정 글 하이라이트
- 특정 회원에게 쪽지 보내기