FROM openjdk:17-jdk

WORKDIR /app

VOLUME /tmp

COPY build/libs/*.jar app.jar

COPY src/main/resources/application.yml application.yml

ENTRYPOINT ["java", "-jar", "app.jar"]