FROM eclipse-temurin:17

COPY ./build/libs/draw-server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar","./app.jar"]
EXPOSE 8080
