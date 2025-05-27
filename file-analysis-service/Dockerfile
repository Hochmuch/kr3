FROM eclipse-temurin:21-jdk-alpine

WORKDIR /main

COPY build/libs/file-analysis-service.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]