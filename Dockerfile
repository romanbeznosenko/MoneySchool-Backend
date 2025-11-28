# ==========================
#   BUILD STAGE
# ==========================
FROM maven:3.9.6-amazoncorretto-21 AS builder

WORKDIR /app

# Copy only pom first to cache dependencies
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:resolve

# Copy source code
COPY src ./src

# Build JAR
RUN mvn -q -e -DskipTests clean package

# ==========================
#   RUN STAGE
# ==========================
FROM amazoncorretto:21

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose app port (optional)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
