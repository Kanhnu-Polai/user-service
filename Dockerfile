FROM eclipse-temurin:17


LABEL maintainer="kanhnupolai79@gmail.com"

WORKDIR /app


COPY target/userservice-0.0.1-SNAPSHOT.jar /app/userservice.jar
EXPOSE 8084

ENTRYPOINT [ "java", "-jar", "userservice.jar" ]