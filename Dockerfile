FROM maven:3.9-eclipse-temurin-24-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean install

FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=build /app/target/java-agent-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "--enable-preview", "-jar", "/app/app.jar"]
