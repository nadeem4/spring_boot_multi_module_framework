## STAGE 1
FROM maven:3.6.3-adoptopenjdk-11 AS MAVEN_BUILD
COPY . /spring_multi_module_framework/
WORKDIR /spring_multi_module_framework/
RUN mvn package

## STAGE 2
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY --from=MAVEN_BUILD /spring_multi_module_framework/application/sample-service-via-script/target/sample_service_via_script.jar /app/
ENTRYPOINT ["java", "-jar", "sample_service_via_script.jar"]
