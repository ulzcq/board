# board 🍃

<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=Spring&logoColor=white"/> 
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=SpringBoot&logoColor=white"/>
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=Thymeleaf&logoColor=white"/>
<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white"/>
<img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white"/>
<img src="https://img.shields.io/badge/Bootstrap-7952B3?style=flat&logo=Bootstrap&logoColor=white"/>

**2022-08-01 ~ 2021-10-13 진행, 개인 프로젝트**

- 스프링을 사용한 회원용 게시판 CRUD 구현
- 세션기반 로그인/회원가입, 회원 수정/비밀번호 수정 기능 구현
- 게시글의  파일 업로드 및 다운로드 구현
- 게시글 페이징 및 검색 기능 구현
- Thymeleaf 및 BootStrap을 사용해 서버 사이드 Front-End 개발

> ver 1. spring data jpa ---→  ver 2. Querydsl로 쿼리 최적화


## 사용 기술

- Spring Boot 2.7.2
- Spring Data JPA
- Querydsl
- Java 11
- Build Tool: gradle
- DB: H2
- Log: Slf4j
- Front: Thymeleaf, HTML, CSS, BootStrap

## 결과물
### 1. 홈 화면
![image](https://user-images.githubusercontent.com/43891587/228147815-4d0ab762-4565-4716-b29d-34a1ffa6fb8d.png)
### 2. 회원가입 화면 (validation 오류가 뜬 경우)
![image](https://user-images.githubusercontent.com/43891587/228148959-82f49a08-feac-45fb-9f36-e73adb0321bd.png)
### 3. 로그인 화면(validation 오류가 뜬 경우)
![image](https://user-images.githubusercontent.com/43891587/228149040-a41058b7-687f-420e-8855-d8b7d6e69bf6.png)
### 4. 게시글 등록 화면
- 첨부파일 3개까지 추가 가능
- 첨부파일은 10MB까지 용량제한 하도록 구현
![image](https://user-images.githubusercontent.com/43891587/228149683-c7206601-6e1e-4aee-931e-1e8ef35ede8b.png)
### 5. 게시글 목록 조회 화면(로그인 후 이동되는 화면)
- 페이징: 이동할 수 있는 10개의 블록을 출력, 한 블록당 게시글 10개씩 조회 
![image](https://user-images.githubusercontent.com/43891587/228149878-5ba145d2-bc5a-4aef-992a-b9774732b30d.png)
### 6. 게시글 조회 화면
- 본인 글의 경우 게시글 수정/삭제 가능
- 첨부파일 다운로드 기능
![image](https://user-images.githubusercontent.com/43891587/228150362-d4fed841-294c-418a-bd7a-6175e04e2626.png)
![image](https://user-images.githubusercontent.com/43891587/228151673-39656508-46dd-4337-b4d2-388491a870e3.png)
### 7. 게시글 수정 화면
![image](https://user-images.githubusercontent.com/43891587/228150875-011227f8-25ae-410a-99f8-b835933bee07.png)
### 8. 프로필 수정 화면
![image](https://user-images.githubusercontent.com/43891587/228171923-e73d24f2-872d-4b0e-8438-caa80414a642.png)
### 9. 비밀번호 수정 화면
![image](https://user-images.githubusercontent.com/43891587/228172155-c5a13ff7-7d1a-45aa-b1bf-72b950617d69.png)
### 10. 제목으로 게시글 검색 화면
- 제목/내용/제목+내용/작성자로 검색 가능
![image](https://user-images.githubusercontent.com/43891587/228172851-514cc70c-f4a3-4e08-bd00-5f277a98f402.png)

