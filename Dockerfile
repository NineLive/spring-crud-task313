FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
#WORKDIR /target
COPY /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]