package sample_service_via_script;

import apidocs.annotations.EnableDocs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import security.annotations.EnableSecurity;

@SpringBootApplication
@EnableDocs
public class Application {
    public static void main( String[] args ) { SpringApplication.run(Application.class, args);}
}

