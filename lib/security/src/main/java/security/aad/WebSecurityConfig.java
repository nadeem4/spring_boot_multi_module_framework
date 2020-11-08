package security.aad;

import com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import javax.servlet.Filter;

@EnableGlobalMethodSecurity (securedEnabled = true,
		prePostEnabled = true)
@Configuration
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AADAuthenticationFilter aadAuthFilter;
	
	private static final String[] AUTH_WHITELIST = {
			"/swagger-resources/**",
			"/swagger-ui.html",
			"/v2/api-docs",
			"/webjars/**",
			"/swagger-ui/**",
			"/api/v1/negotiate",
			"/api/v1/api/messages"
	};
	
	@Override
	protected void configure( HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
		http.authorizeRequests().antMatchers("/api/**").authenticated();
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().anyRequest().permitAll();
		
	    http.addFilterBefore(servletExceptionHandler(), LogoutFilter.class);
		http.addFilterBefore(aadAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
	}
	

	
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthEntryPoint();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Bean
	public ServletExceptionHandler servletExceptionHandler() { return new ServletExceptionHandler();}
	
}