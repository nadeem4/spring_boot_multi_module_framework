package security.annotations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import security.aad.WebSecurityConfig;
import security.config.MDCLogConfig;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
@Documented
@Import({WebSecurityConfig.class, MDCLogConfig.class})
public @interface EnableSecurity {

}
