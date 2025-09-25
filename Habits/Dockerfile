# ===== Stage 1: Build frontend =====
FROM node:22-alpine AS frontend-build
WORKDIR /app/src/ui 

# Copy only frontend package files first (for caching npm install)
COPY src/ui/package*.json ./
RUN npm install

# Copy frontend source and build
COPY src/ui ./
RUN npm run build


# ===== Stage 2: Build backend =====
FROM maven:3.9.6-eclipse-temurin-21 AS backend-build
WORKDIR /app

# Copy Maven files first (for dependency caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of backend source
COPY src ./src

# Copy frontend build output into Spring Boot's static resources
COPY --from=frontend-build /app/src/ui/build ./src/main/resources/static

# Build backend JAR
RUN mvn clean package -DskipTests


# ===== Stage 3: Runtime =====
FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=backend-build /app/target/Habits-0.0.1.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]