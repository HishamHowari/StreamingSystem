FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ./target/authenticationservice-0.0.1-SNAPSHOT.jar auth.jar

ENTRYPOINT ["java", "-jar","/auth.jar"]