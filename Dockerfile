# Multi-stage build for Spring Boot app

# 1) Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
# Pre-fetch dependencies (better layer cache)
RUN mvn -q -e -B -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -e -B -DskipTests clean package

# 2) Runtime stage
FROM eclipse-temurin:21-jre
ENV JAVA_OPTS=""
WORKDIR /app

# Copy fat jar
COPY --from=build /workspace/target/task-manager-backend-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
