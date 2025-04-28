# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src
COPY prod.env .

# Download dependencies
RUN ./mvnw dependency:go-offline

# Build application
RUN ./mvnw clean package -DskipTests

# ------------------------------

# Final image
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/prod.env prod.env

# Expose Spring Boot port
EXPOSE 8080

# IMPORTANT: Activate 'prod' profile during run
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]