# Build stage
FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
RUN apt-get update && apt-get install -y wget curl unzip
WORKDIR /home/gradle/src
RUN gradle :idle-presentation:bootJar --no-daemon

# Package stage
FROM openjdk:17-slim

# 필수 패키지 및 Chromium 설치
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    unzip \
    chromium

# Chromium 버전 확인
RUN chromium --version

# 복사된 JAR 파일 실행
COPY --from=build /home/gradle/src/idle-presentation/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
