FROM gradle:jdk22-alpine as build

WORKDIR /build
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src
RUN gradle clean build --exclude-task test


FROM 22-alpine-full
LABEL authors="htilssu"
EXPOSE 8080

COPY --from=build /build/build/libs/*SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
