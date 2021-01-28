FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir /images
ENTRYPOINT [ "java", "-jar", "/app.jar" ]