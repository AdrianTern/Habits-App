# ===== Stage 1: Build backend (includes frontend via Maven) =====
FROM maven:3.9.6-eclipse-temurin-21 AS backend-build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the project
COPY . .

# Build backend JAR (includes frontend build & static copy)
RUN mvn clean package -DskipTests


# ===== Stage 2: Runtime =====
FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=backend-build /app/target/Habits-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]