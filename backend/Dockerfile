FROM openjdk:17-jdk-oracle
ARG JAR_FILE=./build/libs/backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "-Dspring.config.location=classpath:/application.yml,/secret/application-secret.yml","/app.jar"]