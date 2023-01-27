# 순서
(2023-01-18)
1. https://start.spring.io/ 에서 프로젝트 다운로드
2. main/java/com/example/todo/TodoApplication.java - 톰캣 실행 (오류 발생)
3. main/resources/application.properties - 데이터베이스 연결
4. 톰캣 재실행 (오류 X)
5. Help.md 를 README.md 로 변경
6. .gitignore 에서 README.md 라고 써있는 줄 지우기
7. 설정->빌드,실행,배포->컴파일러) 프로젝트 자동 빌드 [체크]
8. 설정->고급설정->개발된 애플리케이션이 현재 실행중인 경우에도 auto-make 가 시작되도록 허용 [체크]
9. main/java/com/example/todo/HealthCheckController.java 생성 후 작성
10. http://localhost:8080/ 입력해보고 server is running... 뜨는지 확인
11. 상단 VCS -> github 에 프로젝트 공유
12. main/java/com/example/todo/todoapi/entity/TodoEntity.java 생성
13. 깃에 todo entity 생성이라고 올림
14. main/java/com/example/todo/todoapi/repository/TodoRepository.java (interface) 생성 후 작성
15. Ctrl + Shift + t 로 TodoRepository의 테스트파일 생성 후 작성 (TodoRepositoryTest.java)
16. main/java/com/example/todo/todoapi/service/TodoService.java 생성 후 작성
17. main/java/com/example/todo/todoapi/dto/response/TodoListResponseDTO.java 및 TodoDetailResponseDTO 생성 후 작성
18. main/java/com/example/todo/todoapi/dto/request/TodoCreateRequestDTO.java 생성 후 작성
19. main/java/com/example/todo/todoapi/dto/request/TodoModifyRequestDTO.java 생성 후 작성
20. Ctrl + Shift + t 로 TodoService의 테스트파일 생성 후 작성 (TodoRepositoryTest.java)
21. main/java/com/example/todo/todoapi/controller/TodoApiController.java 생성 후 작성

---
http://localhost:8080/join  GET    <=== join.html

http://localhost:3000/signup      <=== Join.js 라우팅\
http://localhost:8080/signup    <=== 회원가입 요청\
http://localhost:3000/login     <=== Login.js 라우팅
---
JWT

{\
"sub" : 토큰 식별값,\
"iss" : 토큰 발급자 정보,\
"iat" : 언제발급했는지 시간정보,\
"exp": 토큰 만료시간\
}\
서명 (위조확인)
---
공통 코드 처리 (로그 출력, 요청헤더 파싱, 세션 검사, 쿠키 검사)
1. 필터 : 프론트 컨트롤러 앞에서 작동
2. 인터셉터 : 하위 컨트롤러 앞뒤에서 작동
3. AOP : 각 메서드 앞뒤에도 작동