# build the application
FROM maven:3.6.0-jdk-8-alpine AS builder
COPY . .
RUN mvn clean package

FROM adoptopenjdk/openjdk8:jdk8u202-b08-alpine-slim
COPY --from=builder target/*-fat.jar /app.jar
# run the application
CMD [ "java", "-jar", "/app.jar" ]

