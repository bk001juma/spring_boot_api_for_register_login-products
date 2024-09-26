# Use the official OpenJDK image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the src directory to the working directory
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Copy the jar file into the container
COPY target/your-app-name.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
