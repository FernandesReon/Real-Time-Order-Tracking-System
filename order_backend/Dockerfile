#Stage 1: Build the project
# official light weight image of maven
FROM maven:3.8.4-openjdk-17-slim AS build

# a working directory in container
WORKDIR /app

# copy all the required dependencies
COPY pom.xml .

# download all the dependencies
RUN mvn dependency:go-offline

# copy the source code
COPY src ./src

# build the project and create a .jar file
RUN mvn clean package -DskipTests

# Stage 2: Run the application
# official smaller jdk image
FROM openjdk:17-jdk-slim

# working directory in container
WORKDIR /app

# copy the .jar file from build stage
COPY --from=build /app/target/order_backend-0.0.1-SNAPSHOT.jar app.jar

# expose a port so container can listen to it.
EXPOSE 8080

# command to run the jar file when container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]