# 관광지 배리어프리 정보 제공 웹사이트

장애인, 노약자 등 교통약자를 위한 **배리어프리 관광지 정보 제공 웹사이트**입니다. 한국문화정보원(KCISA)의 OpenAPI를 활용하여 전국의 관광지 정보를 수집하고, 사용자들이 게시글 및 댓글 기능을 통해 정보를 공유할 수 있는 커뮤니티 기능을 제공합니다.

## 주요 기능

- **관광지 정보 제공**
  - KCISA OpenAPI로부터 관광지 데이터를 주기적으로 받아와 MySQL에 저장
  - 사용자에게 관광지 목록 및 상세 정보 제공

- **게시판 및 댓글**
  - 로그인한 사용자는 게시글 및 댓글 작성 가능
  - 본인이 작성한 댓글만 삭제 가능

- **회원 기능**
  - 회원가입 / 로그인 / 로그아웃
  - 로그인 시 닉네임으로 사용자 식별

## 기술 스택

| 분류       | 기술                         |
|------------|------------------------------|
| Language   | Java 17                      |
| Framework  | Spring Boot 3.4.4            |
| Build Tool | Gradle                       |
| Database   | MySQL                        |
| View       | Thymeleaf (HTML 기반)        |
| API        | KCISA OpenAPI (관광지 정보)  |
| Security   | Spring Security              |

## 프로젝트 구조

```bash
src/
├─── main/
│    ├── java/com/example/demo/
│    │   ├── User/                # 회원 관련 (UserController, UserEntity, UserRepository 등)
│    │   ├── Tour/                # 관광지 관련 (TourController, TourEntity, TourRepository 등)
│    │   ├── Post/                # 게시글/댓글 관련
│    │   ├── API/                 # OpenAPI 연동 클래스
│    │   ├── Config/              # SecurityConfig, SchedulingConfig 등
│    │   └── DTO/                 # 응답 파싱용 DTO들 (TourDTO, TourApiResponseDTO 등)
│    ├─── resources/
│    ├── templates/           # Thymeleaf HTML 파일
│    ├── static/              # JS, CSS 등 정적 리소스
│    └── application.properties
