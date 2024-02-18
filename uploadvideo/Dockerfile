FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ./target/uploadvideo-0.0.1-SNAPSHOT.jar uploadvid.jar

ENTRYPOINT ["java", "-jar","/uploadvid.jar"]
