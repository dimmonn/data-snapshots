FROM openjdk:8-jdk-alpine
VOLUME /tmp
ENV app_name target/data-snapshots-auth.jar
RUN apk update && apk upgrade && apk add curl
COPY ${app_name} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
