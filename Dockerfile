FROM eclipse-temurin:17

ADD . /app
WORKDIR /app
RUN ./gradlew clean build

COPY ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar","./app.jar"]

EXPOSE 8080
