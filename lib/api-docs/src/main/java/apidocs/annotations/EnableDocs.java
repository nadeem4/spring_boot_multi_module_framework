package apidocs.annotations;

import apidocs.config.SwaggerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.*;

@Retention (RetentionPolicy.RUNTIME)
@Target ({ ElementType.TYPE})
@Documented
@Import ({SwaggerConfig.class})
public @interface EnableDocs {
	
	public String title() default "Swagger API Documentation";
	public String desc() default  "Allows to interact with Backend";
	public String ver() default "v1";
	public String pathMapping() default "/";
	
}
