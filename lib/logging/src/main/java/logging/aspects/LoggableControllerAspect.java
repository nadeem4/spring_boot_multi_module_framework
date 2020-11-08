package logging.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.jboss.logging.MDC;
import org.springframework.context.annotation.Configuration;
import utility.mapper.AppObjectMapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Configuration
@Slf4j
public class LoggableControllerAspect {
	
	@Before ("logRequestBody()")
	public void logRequestBodyBefore( JoinPoint joinPoint ) throws Throwable {
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		MDC.put("request_body", Arrays.asList(joinPoint.getArgs()).stream().map(o -> {
			try {return AppObjectMapper.convertObjectToJson(o);}
			catch(Exception e) { return Arrays.toString(joinPoint.getArgs());}
			finally { log.info(Arrays.toString(joinPoint.getArgs()));}
		}).collect(Collectors.toList()));
		MDC.put("target_class", joinPoint.getTarget().getClass().getName());
		
		log.info("{} event started", joinPoint.getSignature().getName().toUpperCase());
		
		MDC.remove("method_name");
		MDC.remove("class_name");
		MDC.remove("request_body");
		MDC.remove("target_class");
	}
	
	@Before ("logRequestParam()")
	public void logRequestParamBefore( JoinPoint joinPoint ) throws Throwable {
		AppObjectMapper mapper = new AppObjectMapper();
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		MDC.put("request_param", Arrays.toString(joinPoint.getArgs()));
		MDC.put("target_class", joinPoint.getTarget().getClass().getName());
		
		log.info("{} event started", joinPoint.getSignature().getName().toUpperCase());
		
		MDC.remove("method_name");
		MDC.remove("class_name");
		MDC.remove("request_param");
		MDC.remove("target_class");
	}
	
	
	@AfterReturning (pointcut = "logRequestBody()", returning = "response")
	public void logAfterReturning( JoinPoint joinPoint, Object response ) throws Throwable {
		AppObjectMapper mapper = new AppObjectMapper();
		
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		
		if ( response != null ) {
			try {
				MDC.put("response", Stream.of(response).map(o -> {
					try {return AppObjectMapper.convertObjectToJson(o);}
					catch(Exception e) { return response.toString();}
					finally { log.info(AppObjectMapper.convertObjectToJson(response));}
				}).collect(Collectors.toList()));
				log.info("Return value of method: {} is {}", joinPoint.getSignature().getName(), MDC.get("response"));
			} catch( Exception e) {
				MDC.put("response", "NotAbleToConvertToString");
				log.info("Return value of method: {} is {}", joinPoint.getSignature().getName(), MDC.get("response"));
			}
		} else {
			log.info("Return value of method: {} is {}", joinPoint.getSignature().getName(),"void");
		}
		
		MDC.remove("method_name");
		MDC.remove("class_name");
		MDC.remove("response");
	}
	
	@Around ("logRequestBody()")
	public Object logAround( ProceedingJoinPoint joinPoint ) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			Object result = joinPoint.proceed();
			long elapsedTime = System.currentTimeMillis() - start;

			MDC.put("method_name", joinPoint.getSignature().getName());
			MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
			MDC.put("execution_time_for_request_in_ms", elapsedTime);

			log.info("{} event completed", joinPoint.getSignature().getName().toUpperCase());

			MDC.remove("method_name");
			MDC.remove("class_name");
			return result;
		} finally {
			log.info("Completed controller around aspect.");
		}
	}
	
	@AfterThrowing (value = "logRequestBody()", throwing = "ex")
	public void logAfterThrowing( JoinPoint joinPoint, Throwable ex ) throws Throwable {
		AppObjectMapper mapper = new AppObjectMapper();
		
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		MDC.put("request_body", Arrays.asList(joinPoint.getArgs()).stream().map(o -> {
			try {return AppObjectMapper.convertObjectToJson(o);}
			catch (Exception e) { return Arrays.toString(joinPoint.getArgs()); }
			finally { log.info(Arrays.toString(joinPoint.getArgs()));}
		}).collect(Collectors.toList()));
		
		Map stackTraceMap = new HashMap();
		List<Map> stackTraceMapList = new ArrayList<>();
		StackTraceElement[] stackTraceElements;
		if(ex.getCause() != null) {
			stackTraceElements = ex.getCause().getStackTrace();
		} else {
			stackTraceElements = ex.getStackTrace();
		}
		Arrays.asList(stackTraceElements).stream().forEach( stackTraceElement -> {
			stackTraceMap.put("class_name", stackTraceElement.getClassName());
			stackTraceMap.put("method_name", stackTraceElement.getMethodName());
			stackTraceMap.put("line_number", stackTraceElement.getLineNumber());
			stackTraceMap.put("module_name", stackTraceElement.getModuleName());
			stackTraceMapList.add(stackTraceMap);
		});
		
		MDC.put("stack_trace",  Arrays.asList(stackTraceMapList).stream().map(o -> {
			try {return AppObjectMapper.convertObjectToJson(o);}
			catch( Exception e) { return stackTraceMapList.toString();}
			finally { log.info(stackTraceMapList.toString());}
		}).collect(Collectors.toList()));
		MDC.put("message", ex.getLocalizedMessage());
	}
	
	@Pointcut (
			"@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
					"@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
					"@annotation(org.springframework.web.bind.annotation.PutMapping)"
	)
	public void logRequestBody() {}
	
	@Pointcut (
			"@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
					"@annotation(org.springframework.web.bind.annotation.DeleteMapping)"
	)
	public void logRequestParam() {}
}
