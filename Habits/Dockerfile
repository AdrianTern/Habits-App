# Use a minimal Java runtime image
FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy your built jar into the container
COPY target/Habits-0.0.1.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]