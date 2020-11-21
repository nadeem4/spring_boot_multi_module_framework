
@echo off
set /p name="Enter new Microservice name: "
set /p contact_name="(Contact Details ) Enter name: "
set /p email="(Contact Details ) Enter email: "


:: Creating folder structure till main 
mkdir "../application/%name%/src/main/java"
mkdir "../application/%name%/src/main/resources"

mkdir "../application/%name%/src/main/java/%name%/controller/v1/api"
mkdir "../application/%name%/src/main/java/%name%/controller/v1/mapper"
mkdir "../application/%name%/src/main/java/%name%/controller/v1/request"

mkdir "../application/%name%/src/main/java/%name%/dto/mapper"
mkdir "../application/%name%/src/main/java/%name%/dto/model"

mkdir "../application/%name%/src/main/java/%name%/model"
mkdir "../application/%name%/src/main/java/%name%/repository"
mkdir "../application/%name%/src/main/java/%name%/service"



:: Writing into resources application.yml  

(
echo spring:
echo   application:
echo     name: %name%
echo   datasource:
echo     url: jdbc:h2:mem:testdb
echo     driver-class-name: org.h2.Driver
echo     username: test
echo     password: test
echo   jpa:
echo     database-platform: org.hibernate.dialect.H2Dialect
echo   h2:
echo     console:
echo       enabled: true
echo   autoconfigure:
echo     exclude:
echo       - org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
echo       - org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
echo server:
echo   port: 8080
echo   servlet:
echo     session:
echo       cookie:
echo         http-only: true
echo         secure: true
echo app:
echo   version: 1.0.0
echo   security:
echo     enabled: true
echo   swagger:
echo     enable-auth: false
echo     title: Sample Service
echo     desc: Helps to interact with Backend
echo     version: v1
echo     path-mapping: /
echo     contact:
echo       name: %contact_name%
echo       email: %email%
echo       url: ""
echo     license: null
echo     license-uri: null

) > "../application/%name%/src/main/resources/application.yml" 

:: Writing into resources bootstrap.yml  
echo "" > "../application/%name%/src/main/resources/bootstrap.yml"


:: Writing into controller

(
echo package sample_service.controller.v1.api;
echo:
echo import org.springframework.beans.factory.annotation.Autowired;
echo import org.springframework.http.HttpStatus;
echo import org.springframework.http.ResponseEntity;
echo import org.springframework.web.bind.annotation.GetMapping;
echo import org.springframework.web.bind.annotation.PostMapping;
echo import org.springframework.web.bind.annotation.RequestBody;
echo import org.springframework.web.bind.annotation.RequestParam;
echo import sample_service.controller.v1.mapper.AppDTOMapper;
echo import sample_service.controller.v1.request.AppRequest;
echo import sample_service.service.AppService;
echo import utility.annotations.ControllerV1;
echo import utility.constant.ActionType;
echo import utility.constant.EntityType;
echo import utility.constant.Messages;
echo import utility.custom_data_type.ValidList;
echo import utility.dto.ResponseDTO;
echo:
echo import javax.validation.Valid;
echo import javax.validation.constraints.NotNull;
echo:
echo @ControllerV1
echo public class AppControllerV1 {
echo:
echo     @Autowired
echo     private AppService service;
echo:
echo     @GetMapping^(path = "/greet"^)
echo     public ResponseEntity^<String^> greetUser^( @NotNull @RequestParam String name^) {
echo         return ResponseEntity.ok^(service.greetService^(name^)^);
echo     }
echo:
echo     @PostMapping^(path = "/user"^)
echo     public ResponseEntity^<ResponseDTO^> setUserDetail^(@Valid @RequestBody AppRequest request^) {
echo         service.saveUser^(new AppDTOMapper^(^).convertToDTO^(request^)^);
echo         return new ResponseEntity^( ResponseDTO.setResponseDTO^(
echo                 Messages.setMessage^(EntityType.USER, ActionType.CREATED, String.valueOf^(request.getId^(^)^)^)^),
echo                 HttpStatus.CREATED
echo         ^);
echo     }
echo:
echo     @PostMapping^(path = "/user"^)
echo     public ResponseEntity^<ResponseDTO^> setUsersDetail^(@Valid @RequestBody ValidList^<AppRequest^> request^) {
echo:
echo         return new ResponseEntity^( ResponseDTO.setResponseDTO^(
echo                 Messages.setMessage^(EntityType.USER, ActionType.CREATED, ""^)^),
echo                 HttpStatus.CREATED
echo         ^);
echo     }
echo }
echo:
) > "../application/%name%/src/main/java/%name%/controller/v1/api/AppControllerV1.java"



:: writing into pom.xml  
(
echo ^<?xml version="1.0" encoding="UTF-8"?^>
echo ^<project xmlns="http://maven.apache.org/POM/4.0.0"
echo          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
echo          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"^>
echo     ^<parent^>
echo         ^<artifactId^>backend-parent^</artifactId^>
echo         ^<groupId^>com.app^</groupId^>
echo         ^<version^>1.0.0-SNAPSHOT^</version^>
echo         ^<relativePath^>../../pom.xml^</relativePath^>
echo     ^</parent^>
echo     ^<modelVersion^>4.0.0^</modelVersion^>
echo: 
echo     ^<artifactId^>%name%^</artifactId^>
echo: 
echo     ^<dependencies^>
echo         ^<!-- Enables basic Swagger UI, by adding @EnableDocs annotation on Main Class. For Advance configuration, check README file --^>
echo         ^<dependency^>
echo             ^<groupId^>com.app^</groupId^>
echo             ^<artifactId^>api-docs^</artifactId^>
echo             ^<version^>${project.version}^</version^>
echo         ^</dependency^>
echo:
echo         ^<!-- It contains security, logging and exception dependencies.--^>
echo         ^<dependency^>
echo             ^<groupId^>com.app^</groupId^>
echo             ^<artifactId^>backend-starter^</artifactId^>
echo             ^<version^>${project.version}^</version^>
echo         ^</dependency^>
echo:
echo         ^<!-- Check README file for details of this module. --^>
echo         ^<dependency^>
echo             ^<groupId^>com.app^</groupId^>
echo             ^<artifactId^>utility^</artifactId^>
echo             ^<version^>${project.version}^</version^>
echo         ^</dependency^>
echo:
echo         ^<!-- Uncomment this section, if you want support for azure blob storage. Check README file for detail usage. --^>
echo        ^<!-- ^<dependency^>
echo             ^<groupId^>com.app^</groupId^>
echo             ^<artifactId^>file-handler^</artifactId^>
echo             ^<version^>${project.version}^</version^>
echo         ^</dependency^>--^>
echo     ^</dependencies^>
echo:
echo     ^<build^>
echo         ^<plugins^>
echo             ^<plugin^>
echo                 ^<groupId^>org.springframework.boot^</groupId^>
echo                 ^<artifactId^>spring-boot-maven-plugin^</artifactId^>
echo                 ^<configuration^>
echo                     ^<layers^>
echo                         ^<enable^>true^</enable^>
echo                     ^</layers^>
echo                 ^</configuration^>
echo             ^</plugin^>
echo         ^</plugins^>
echo     ^</build^>
echo:
echo ^</project^>
) > "../application/%name%/pom.xml"


:: writing into DTO mapper  
(
echo package sample_service.dto.mapper;
echo: 
echo import logging.annotations.Loggable;
echo import sample_service.dto.model.AppModelDTO;
echo import sample_service.model.AppModel;
echo: 
echo public class AppModelMapper {
echo: 
echo     @Loggable^(valueAfter = "Value After", valueAfterReturning = "Value After Returning", valueAround = "Value Around", valueBefore = "Value Before"^)
echo     public AppModel convertToModel^(AppModelDTO modelDTO^) {
echo         AppModel model = new AppModel^(^);
echo         model.setId^(modelDTO.getId^(^)^);
echo         model.setName^(modelDTO.getName^(^)^);
echo         return model;
echo     }
echo }
echo:
) > "../application/%name%/src/main/java/%name%/controller/v1/mapper/AppModelMapper.java"

:: writing into dto model 
(
echo package sample_service.dto.model;
echo: 
echo import lombok.Getter;
echo import lombok.NoArgsConstructor;
echo import lombok.Setter;
echo: 
echo @Setter
echo @Getter
echo @NoArgsConstructor
echo public class AppModelDTO {
echo: 
echo     private Integer id;
echo: 
echo     private String name;
echo }
echo:
) > "../application/%name%/src/main/java/%name%/dto/model/AppModelDTO.java"

:: writing into model 

(
echo package sample_service.model;
echo: 
echo import lombok.Getter;
echo import lombok.NoArgsConstructor;
echo import lombok.Setter;
echo: 
echo @Getter
echo @Setter
echo @NoArgsConstructor
echo public class AppModel {
echo     private Integer id;
echo: 
echo     private String name;
echo }
echo: 
) > "../application/%name%/src/main/java/%name%/model/AppModel.java" 

:: writing into Repository 
(
echo package sample_service.repository;
echo:
echo import org.springframework.beans.factory.annotation.Autowired;
echo import org.springframework.jdbc.core.JdbcTemplate;
echo import org.springframework.jdbc.core.simple.SimpleJdbcCall;
echo import org.springframework.stereotype.Repository;
echo import sample_service.model.AppModel;
echo:
echo import javax.annotation.PostConstruct;
echo:
echo @Repository
echo public class AppRepository {
echo:
echo     @Autowired
echo     private JdbcTemplate jdbcTemplate;
echo:
echo     private SimpleJdbcCall simpleJdbcCall;
echo:
echo     @PostConstruct
echo     public void init^(^) { jdbcTemplate.setResultsMapCaseInsensitive^(true^);}
echo:
echo     public String greetRepository^( String name ^) {
echo         return name;
echo     }
echo:
echo     public void saveUser^(AppModel model^) {
echo:
echo     }
echo }
echo:
) > "../application/%name%/src/main/java/%name%/repository/AppRepository.java"

:: Writing into Service 
(
echo package sample_service.service;

echo import org.springframework.beans.factory.annotation.Autowired;
echo import org.springframework.core.io.Resource;
echo import org.springframework.http.ResponseEntity;
echo import org.springframework.stereotype.Service;
echo import org.springframework.web.multipart.MultipartFile;
echo import sample_service.dto.model.AppModelDTO;
echo import sample_service.repository.AppRepository;
echo import java.net.URI;
echo:
echo @Service
echo public class AppService {
echo: 
echo     @Autowired
echo     private AppRepository repository;
echo:
echo     public String greetService^( String name ^) {
echo         return repository.greetRepository^(name^);
echo     }
echo:
echo     public void saveUser^(AppModelDTO model^) {
echo: 
echo     }
echo /*
echo     public ResponseEntity^<Resource^> download^(String url, String blobName^) {
echo         return fileUtil.downloadBlob^(url, blobName^);
echo     }
echo: 
echo     public void delete^(String url, String blobName^) {
echo         fileUtil.deleteBlob^(url, blobName^);
echo     }
echo: 
echo     public URI uploadBlob^(MultipartFile file, String blobName^) {
echo         return fileUtil.uploadFile^(file, blobName^);
echo     }
echo: 
echo */
echo }

) > "../application/%name%/src/main/java/%name%/service/AppService.java" 



:: Writing into Application.java 
(
echo package sample_service;
echo:
echo import apidocs.annotations.EnableDocs;
echo import org.springframework.boot.SpringApplication;
echo import org.springframework.boot.autoconfigure.SpringBootApplication;
echo import security.annotations.EnableSecurity;
echo:
echo @SpringBootApplication
echo @EnableDocs
echo public class Application {
echo     public static void main^( String[] args ^) { SpringApplication.run^(Application.class, args^);}
echo }
echo:
) > "../application/%name%/src/main/java/%name%/Application.java"







