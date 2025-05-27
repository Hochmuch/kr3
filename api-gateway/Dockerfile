FROM eclipse-temurin:21-jdk-alpine

WORKDIR /main

COPY build/libs/api-gateway.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]