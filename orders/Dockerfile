FROM gradle:8.3-jdk17 AS builder

WORKDIR /app
COPY ./build.gradle ./settings.gradle /app/
RUN gradle dependencies --no-daemon || true
COPY . .
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
