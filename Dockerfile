# ========== BUILD ==========
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
RUN chmod +x mvnw

RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -DskipTests clean package

# ========== FINAL ==========
FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
