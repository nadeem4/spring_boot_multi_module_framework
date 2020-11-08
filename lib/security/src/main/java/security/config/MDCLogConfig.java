package security.config;


import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import utility.constant.AppConstant;

@Component
public class MDCLogConfig extends OncePerRequestFilter {
	

	
	@Override
	public void doFilterInternal( final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
			throws java.io.IOException, ServletException {
		
		UserPrincipal userPrincipal = ((UserPrincipal) request.getSession().getAttribute("CURRENT_USER_PRINCIPAL"));
		
		
		MDC.put(AppConstant.USER_PRINCIPLE, userPrincipal == null ?  "" : userPrincipal.getUniqueName());
		
		
		
		filterChain.doFilter(request, response);
	}
}