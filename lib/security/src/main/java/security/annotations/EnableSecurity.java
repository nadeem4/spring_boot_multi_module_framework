package security.annotations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import security.aad.WebSecurityConfig;
import security.config.MDCLogConfig;

@Configuration
@ConditionalOnProperty(name = "app.security.enabled",
        havingValue = "true", matchIfMissing = true)
public class EnableSecurity {

    @Bean
    public WebSecurityConfig webSecurityConfig() {
        return new WebSecurityConfig();
    }

    @Bean
    public MDCLogConfig mdcLogConfig() {
        return new MDCLogConfig();
    }
}
