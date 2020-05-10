# build the application
FROM maven:3.6.0-jdk-8-alpine AS builder
COPY . .
RUN mvn clean package

FROM oracle/graalvm-ce:20.0.0
RUN gu install python
RUN gu install ruby
# RUN gu install wasm (TODO)
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
COPY --from=builder target/*-fat.jar /app.jar
# run the application
CMD [ "java", "-jar", "/app.jar" ]

