# ============================================
# Multi-Stage Dockerfile for Spring Boot App
# ============================================
# This Dockerfile uses a multi-stage build to:
# 1. Build the application with Maven
# 2. Run the application with a minimal JRE image
# This approach reduces the final image size significantly

# ============================================
# Stage 1: Build
# ============================================
# Use Maven with JDK 17 to compile and package the application
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven configuration and source code
# Copying pom.xml first allows Docker to cache dependencies
COPY pom.xml .
COPY src ./src

# Build the application
# -DskipTests: Skip tests to speed up build (tests should run in CI/CD)
RUN mvn clean package -DskipTests

# ============================================
# Stage 2: Runtime
# ============================================
# Use a minimal JRE image for running the application
# This significantly reduces the final image size
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
# The --from=build flag references the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Run the application
# Using ENTRYPOINT instead of CMD ensures the container runs as an executable
ENTRYPOINT ["java", "-jar", "app.jar"]
