FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/CloudStorage-0.0.1-SNAPSHOT.jar server.jar
ENTRYPOINT ["java", "-jar", "server.jar"]