# Build stage
FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :idle-presentation:bootJar --no-daemon

# Package stage
FROM openjdk:17

COPY /usr/bin/chromedriver /usr/bin/
COPY /usr/bin/chromium-browser /usr/bin/

COPY --from=build /home/gradle/src/idle-presentation/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
