FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew bootJar

RUN cp build/libs/app.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
