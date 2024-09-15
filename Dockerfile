# Build stage
FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :idle-presentation:bootJar --no-daemon

# Package stage
FROM openjdk:17

# Install Chrome browser and Chromedriver
RUN apt-get update && \
    apt-get install -y wget gnupg unzip google-chrome-stable && \
    wget -q "https://chromedriver.storage.googleapis.com/114.0.5735.90/chromedriver_linux64.zip" -O /tmp/chromedriver.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
    rm /tmp/chromedriver.zip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set environment variables
ENV CHROME_DRIVER_PATH=/usr/local/bin/chromedriver
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

# Copy the application JAR
COPY --from=build /home/gradle/src/idle-presentation/build/libs/*.jar /app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-Dwebdriver.chrome.driver=/usr/local/bin/chromedriver", "-jar", "/app.jar"]
