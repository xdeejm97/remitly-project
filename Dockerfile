# Stage 1: Build the Java backend
FROM maven:3.9.7-amazoncorretto-17 AS builder

WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM amazoncorretto:17-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./app.jar"]