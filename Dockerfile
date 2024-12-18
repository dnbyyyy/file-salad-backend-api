FROM openjdk:21-jdk-slim

# Install dependencies
RUN apt-get update && \
    apt-get install -y wget unzip

# Download Gradle 7.5.1
WORKDIR /opt
RUN wget https://services.gradle.org/distributions/gradle-7.5.1-bin.zip && \
    unzip gradle-7.5.1-bin.zip

# Set environment variables for Gradle
ENV GRADLE_HOME=/opt/gradle-7.5.1
ENV PATH=$GRADLE_HOME/bin:$PATH

# Set the working directory inside the container
WORKDIR /app

# Copy your project files
COPY . .

# Build the project
RUN chmod +x gradlew
RUN ./gradlew clean
RUN ./gradlew bootJar

ENTRYPOINT ["java", "-jar", "build/libs/file-salad-backend-api-0.0.1-SNAPSHOT.jar"]