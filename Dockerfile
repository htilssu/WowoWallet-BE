FROM gradle:jdk22-alpine as build

WORKDIR /build
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src
ENV DOCKER_COMPOSE_ENABLED=false
RUN gradle clean build --exclude-task test


FROM amazoncorretto:22-alpine-full
LABEL authors="htilssu"
ENV DOCKER_COMPOSE_ENABLED=false
EXPOSE 8080



COPY --from=build /build/build/libs/*SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java","-Xms256m -Xmx1024m", "-jar", "app.jar"]
