FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
EXPOSE 8080
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV API_KEY_GEO_CODER=${API_KEY_GEO_CODER}
ENV API_KEY_WEATHER=${API_KEY_WEATHER}
ENTRYPOINT ["java","-jar","/app.jar"]