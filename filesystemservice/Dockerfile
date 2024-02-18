FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ./target/filesystemservice-0.0.1-SNAPSHOT.jar fileservice.jar
ENTRYPOINT ["java", "-jar","/fileservice.jar"]