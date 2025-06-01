FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/footballapi-0.0.1-SNAPSHOT.jar app.jar

# Pass API key as environment variable
ENV APIFOOTBALL_API_KEY=changeme

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]