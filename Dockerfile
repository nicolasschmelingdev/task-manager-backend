FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .

RUN mvn -q -e -B -Dmaven.test.skip=true dependency:go-offline
COPY src ./src
RUN mvn -q -e -B -Dmaven.test.skip=true clean package

FROM eclipse-temurin:21-jre
ENV JAVA_OPTS=""
WORKDIR /app

COPY --from=build /workspace/target/task-manager-backend-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
