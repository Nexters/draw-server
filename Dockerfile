FROM eclipse-temurin:17

RUN echo $(ls -al)
RUN ./gradlew clean build

COPY ./build/libs/*.jar app.jar

RUN echo $(ls -al)
ENTRYPOINT ["java", "-jar","./app.jar"]

EXPOSE 8080
