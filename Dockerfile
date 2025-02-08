# Stage 1: Build the application
FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the entire project (including the firebase folder)
COPY . .

# Build the application
RUN mvn clean package -B -DskipTests

# Stage 2: Run the application
FROM openjdk:17
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar e-commerce.jar

# Copy the firebase folder from the builder stage
COPY --from=builder /app/firebase /app/firebase

# Run the application
ENTRYPOINT ["java", "-jar", "/app/e-commerce.jar"]