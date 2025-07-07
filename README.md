# 🧱 Limber Server

Spring 기반 **Modular Monolith** 아키텍처로 구성된 백엔드 프로젝트입니다.  
도메인 중심 모듈 구조와 Gradle 멀티모듈 설정, Docker 기반의 로컬 개발 환경을 제공합니다.

---

## 📁 프로젝트 구조

```
limber-server/
├── build.gradle          # 루트 Gradle 설정 (공통 dependency 관리)
├── settings.gradle       # 포함 모듈 설정
├── docker/               # Docker 및 DB 설정 폴더
│   ├── Dockerfile
│   └── docker-compose.yml
├── common/               # 공통 유틸 및 설정 모듈
├── user/                 # user 도메인 모듈
├── core/                 # 메인 실행/조립 모듈
└── README.md
```

---

## 🧱 아키텍처 개요

- **Modular Monolith**  
  각 도메인을 독립 모듈로 분리하여 내부 응집력은 유지하되, 하나의 프로세스로 배포됩니다.

- **Gradle 멀티모듈 구성**
    - 공통 의존성은 루트 `build.gradle`에서 통합 관리
    - `common`, `user`, `core` 등 하위 모듈은 `settings.gradle`에서 include

- **Spring Modulith 적용**
    - `spring-modulith`를 통해 모듈 간 명확한 의존성 분리
    - `@EnableUserModule` 등을 통해 조립

---

## 🐳 Docker 개발 환경

Dockerfile 및 docker-compose를 통해 로컬 개발 환경이 빠르게 구성됩니다.

```bash
# 빌드 및 실행
./gradlew clean build
docker-compose up --build
```

> PostgreSQL 15이 자동 실행되며, 애플리케이션은 `core` 모듈에서 시작됩니다.

---

## 🔧 Requirements

- Java 17+
- Gradle 8+
- Docker / Docker Compose

---

## 📝 향후 추가 예정

- 테스트 통합 및 CI/CD 파이프라인 연동
