# Dockerfile
FROM eclipse-temurin:17-jdk

WORKDIR /app

# gradlew와 프로젝트 전체 복사
COPY . .

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# bootJar 빌드 실행
RUN ./gradlew :core:bootJar

# 빌드된 JAR 파일을 복사 (출력 경로에 맞춰서 수정)
RUN cp core/build/libs/app.jar .

ENTRYPOINT ["java", "-jar", "app.jar"]
