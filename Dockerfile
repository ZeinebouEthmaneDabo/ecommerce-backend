# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/e-commerce-0.0.1-SNAPSHOT.jar app.jar

COPY firebase/ecommerce-13ffd-firebase-adminsdk-5kaia-21a754f362.json /app/firebase/ecommerce-13ffd-firebase-adminsdk-5kaia-21a754f362.json


# Expose the port your app runs on
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]