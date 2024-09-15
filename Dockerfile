## Build stage
#FROM gradle:jdk17 AS build
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
#RUN gradle :idle-presentation:bootJar --no-daemon
#
## Package stage
#FROM openjdk:17-slim
#COPY --from=build /home/gradle/src/idle-presentation/build/libs/*.jar /app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]

# Build stage
FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :idle-presentation:bootJar --no-daemon

# Package stage
FROM openjdk:17-slim

# Install required packages
RUN apt-get update && \
    apt-get install -y \
    wget \
    gnupg \
    curl \
    lsb-release \
    && apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Add the Google Chrome repository and install Chromium and ChromeDriver
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-archive-keyring.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-archive-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" | tee /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y \
    chromium \
    chromium-driver \
    && apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set environment variables for Chromium and ChromeDriver
ENV CHROME_BIN=/usr/bin/chromium
ENV CHROMEDRIVER_BIN=/usr/lib/chromium/chromedriver

# Copy the built JAR file
COPY --from=build /home/gradle/src/idle-presentation/build/libs/*.jar /app.jar

# Expose port and define entrypoint
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
