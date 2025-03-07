# Use the official OpenJDK 21 image
FROM openjdk:21-jdk

USER root
RUN chmod +x /var/lib/jenkins/workspace/restapiscicd/mvnw
USER jenkins

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/*.jar app.jar

# Expose the application port (default Spring Boot port is 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
