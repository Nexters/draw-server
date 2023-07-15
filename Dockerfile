FROM eclipse-temurin:17

ADD . /app
WORKDIR /app
RUN ./gradlew clean build

RUN echo "----------"
RUN echo $(pwd)
RUN echo $(ls -al)
RUN echo "----------"

COPY ./build/libs/draw-server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar","./app.jar"]

EXPOSE 8080
