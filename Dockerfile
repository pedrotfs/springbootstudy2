#copy to root
FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=build/libs/maestro-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]