FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ./target/videostreaming-0.0.1-SNAPSHOT.jar videostreaming.jar

ENTRYPOINT ["java", "-jar","/videostreaming.jar"]