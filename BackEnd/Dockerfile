## Backend multi-stage Dockerfile meaning that there will be two stages in the Dockerfile, one for building the application and another for running it. The first stage uses a Maven image to build the Java application, while the second stage uses a lightweight JRE image to run the built application.

# The first stage is thrown away after the build is complete, and only the necessary artifacts (the compiled JAR file) are copied to the second stage. This results in a smaller final image size, as it does not include the build tools and dependencies that are only needed during the build process.

# Multi-stage builds are a best practice in Docker, as they help to keep the final image size small and reduce the attack surface by excluding unnecessary build tools and dependencies from the runtime image only the compiled JAR is included in the final image, which is all that is needed to run the application. This makes the final image smaller and more secure.

# Build stage (compiling the app and generating the JAR file), the stage name is "build"
FROM maven:3.9.4-eclipse-temurin-17 as build
WORKDIR /app
# copying the necessary files for building the application. The pom.xml file is the Maven project descriptor, and the mvnw script is the Maven wrapper that allows you to run Maven commands without having to install Maven on your system. The .mvn directory contains the Maven wrapper files, and the src directory contains the source code of the application.
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src
# Use BuildKit cache mount for Maven repository to speed up dependency resolution and
# make builds more resilient to transient network failures.
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package

#----------------------------------------------------
# Runtime stage (Run the compiled JAR file using a lightweight JRE image which is only needed for running a java JAR)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 9080

# this command is used to run the application when the container starts. It tells Docker to execute the Java command with the -jar option, specifying the path to the JAR file that was copied from the build stage. This allows the application to start and listen for incoming requests on the specified port (9080).
ENTRYPOINT ["java","-jar","/app/app.jar"] 
