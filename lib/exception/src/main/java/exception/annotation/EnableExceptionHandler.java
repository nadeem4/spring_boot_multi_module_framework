package exception.annotation;

import exception.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention (RetentionPolicy.RUNTIME)
@Target ({ ElementType.TYPE})
@Documented
@Import (GlobalExceptionHandler.class)
@Configuration
public @interface EnableExceptionHandler {
}
