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
public class LoggableRepositoryAspect {
	
	@Before ("logRepository()")
	public void logBefore( JoinPoint joinPoint ) throws Throwable {
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		MDC.put("parameter",  Arrays.asList(joinPoint.getArgs()).stream().map(o -> {
			try {return AppObjectMapper.convertObjectToJson(o);}
			catch(Exception e) { return Arrays.toString(joinPoint.getArgs());}
			finally { log.info(Arrays.toString(joinPoint.getArgs()));}
		}).collect(Collectors.toList()));
		MDC.put("target_class", joinPoint.getTarget().getClass().getName());
		
		log.info("{} function started", joinPoint.getSignature().getName().toUpperCase());
		
		MDC.remove("method_name");
		MDC.remove("class_name");
		MDC.remove("parameter");
		MDC.remove("target_class");
	}
	
	
	@Around ("logRepository()")
	public Object logAround( ProceedingJoinPoint joinPoint ) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long elapsedTime = System.currentTimeMillis() - start;
		
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		MDC.put("execution_time_for_repository_in_ms", elapsedTime);
		
		log.info("{} function completed", joinPoint.getSignature().getName().toUpperCase());
		
		MDC.remove("method_name");
		MDC.remove("class_name");
		MDC.remove("execution_time_for_repository_in_ms");
		return result;
	}
	
	@AfterReturning (pointcut = "logRepository()", returning = "response")
	public void logAfterReturning( JoinPoint joinPoint, Object response ) throws Throwable {
		AppObjectMapper mapper = new AppObjectMapper();
		
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		
		if (response != null) {
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
			log.info("Return value of method: {} is {}", joinPoint.getSignature().getName(), "void");
		}
		
		MDC.remove("method_name");
		MDC.remove("class_name");
		MDC.remove("response");
	}
	
	@AfterThrowing (value = "logRepository()", throwing = "ex")
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
		
		
		MDC.put("stack_trace",   Arrays.asList(stackTraceMapList).stream().map(o -> {
			try {return AppObjectMapper.convertObjectToJson(o);}
			catch( Exception e) { return stackTraceMapList.toString();}
			finally { log.info(stackTraceMapList.toString());}
		}).collect(Collectors.toList()));
		MDC.put("message", ex.getLocalizedMessage());
		
	}
	
	
	@Pointcut (
			"within(@org.springframework.stereotype.Repository *)"
	)
	public void logRepository() {}
}
