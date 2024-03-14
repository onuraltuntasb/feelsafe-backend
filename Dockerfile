FROM openjdk:17
COPY target/feelsafe-0.0.1-SNAPSHOT.jar feelsafe-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=prod", "-jar","feelsafe-0.0.1-SNAPSHOT.jar"]