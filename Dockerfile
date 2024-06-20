# Build stage
FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :idle-api:bootJar --no-daemon --warning-mode

# Package stage
FROM openjdk:17
COPY --from=build /home/gradle/src/idle-api/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
