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

# Copy the packaged Java application
COPY --from=builder /app/target/*.jar ./app.jar

# Expose the port the app runs on
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./app.jar"]