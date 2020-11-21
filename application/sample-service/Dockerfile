## STAGE 1
FROM maven:3.6.3-adoptopenjdk-11 AS MAVEN_BUILD
COPY . /backend/
WORKDIR /backend/
RUN mvn package

## STAGE 2
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY --from=MAVEN_BUILD /backend/application/sample-service/target/sample-service.jar /app/
ENTRYPOINT ["java", "-jar", "sample-service.jar"]