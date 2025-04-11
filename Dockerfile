# Use a specific OpenJDK version as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR into the container
COPY target/*.jar app.jar

# Expose the port your Spring Boot application runs on (default Spring Boot port)
EXPOSE 9098

# Define environment variables that your application might need
ENV SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
ENV KAFKA_TOPIC=your_kafka_topic

# Define the command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
