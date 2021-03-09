package security.aad;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.dto.ErrorDTO;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import utility.constant.AppConstant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class ServletExceptionHandler extends GenericFilterBean {
	
	private static final String TOKEN_HEADER = "Authorization";
	private static final String TOKEN_TYPE = "Bearer ";
	
	@Value ("${spring.application.name}")
	private String appName;
	
	@Value("${app.version}")
	private String version;
	
	@Value("${spring.profiles.active:dev}")
	private String env;
	
	private List<String> excludedUrls = new ArrayList<>();
	
	@Override
	public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String authHeader = request.getHeader(TOKEN_HEADER);
		UUID uuid = UUID.randomUUID();
		String requestId = uuid.toString();
		MDC.put(AppConstant.REQUEST_ID, requestId);
		MDC.put("microservice", appName);
		MDC.put("version", version);
		MDC.put("env", env);
		MDC.put("request_uri", request.getRequestURI());
		
		if( !isExcludedUrl(request)) {
			if ( authHeader == null || authHeader.isEmpty() ) {
				setError(response, "Token is missing");
				return;
			}
			if( ! authHeader.startsWith(TOKEN_TYPE)) {
				setError(response, "Bearer token is required");
				return;
			}
		}
		
		try {
			filterChain.doFilter(request, response);
		} catch( ServletException e) {
			setError(response, "Token is invalid");
			return;
		}
	}
	
	private String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
	
	private boolean isExcludedUrl(HttpServletRequest request ) {
		return Arrays.stream(
				"/swagger-ui,/v2/api-docs,/swagger-resources,/api/v1/negotiate,/api/v1/api/messages,/health"
						.split(",")
		).anyMatch(s -> request.getRequestURI().contains(s));
	}
	
	private void setError( HttpServletResponse response , String errMsg) throws IOException {
		ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED, errMsg);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(convertObjectToJson(error));
	}
	
}
