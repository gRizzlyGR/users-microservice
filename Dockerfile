FROM maven:3.5.3-jdk-10-slim as base

WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn package -q -Dmaven.test.skip=true

FROM openjdk:10.0.1-10-jre-slim

WORKDIR /app
EXPOSE 8081
COPY --from=base /app/target/users-microservice-1.0.jar /app

CMD ["java", "-jar", "users-microservice-1.0.jar"]