# Use an official OpenJDK image as the base image
FROM openjdk:11

# Set the working directory in the Docker container
WORKDIR /app

# Copy the Gradle build files into the container
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle

# Copy the gradlew script and make it executable
COPY gradlew /app/
RUN chmod +x /app/gradlew

# Copy the source code
COPY src /app/src

# Build the application using Gradle
RUN ./gradlew build

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "build/libs/property-management-0.0.1-SNAPSHOT.jar"]
