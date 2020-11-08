package logging.aspects;

import logging.annotations.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.logging.MDC;
import org.springframework.context.annotation.Configuration;
import utility.mapper.AppObjectMapper;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Aspect
@Configuration
@Slf4j
public class LoggableAspect {
	
	@Before("logMethod()")
	public void logBefore( JoinPoint joinPoint ) throws Throwable {
		MethodSignature methodSignature =  (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		MDC.put("parameter", Arrays.asList(joinPoint.getArgs()).stream().map(o -> {
			try {return AppObjectMapper.convertObjectToJson(o);}
			catch(Exception e) { return Arrays.toString(joinPoint.getArgs());}
			finally { log.info(Arrays.toString(joinPoint.getArgs()));}
		}).collect(Collectors.toList()));
		MDC.put("target_class", joinPoint.getTarget().getClass().getName());
		
		Loggable loggable = method.getAnnotation(Loggable.class);
		if (loggable.valueBefore().isEmpty()) {
			log.info("Executing {} function in {}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
		} else {
			log.info(loggable.valueBefore());
		}
		MDC.remove("method_name");
		MDC.remove("class_name");
		MDC.remove("parameter");
		MDC.remove("target_class");
	}
	
	@After ("logMethod()")
	public void logAfter( JoinPoint joinPoint ) throws Throwable {
		MethodSignature methodSignature =  (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		
		Loggable loggable = method.getAnnotation(Loggable.class);
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		
		if (loggable.valueAfter().isEmpty()) {
			log.info("Executed {} function in {}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
		} else {
			log.info(loggable.valueAfter());
		}
		
		MDC.remove("method_name");
		MDC.remove("class_name");
		
	}
	
	@AfterReturning ( pointcut = "logMethod()", returning = "response")
	public void logAfterReturning( JoinPoint joinPoint, Object response ) throws Throwable {
		MethodSignature methodSignature =  (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		AppObjectMapper mapper = new AppObjectMapper();
		
		Loggable loggable = method.getAnnotation(Loggable.class);
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		if( response != null ) {
			try {
				MDC.put("response", Stream.of(response).map(o -> {
					try {return AppObjectMapper.convertObjectToJson(o);}
					catch(Exception e) { return response.toString();}
					finally { log.info(AppObjectMapper.convertObjectToJson(response));}
				}).collect(Collectors.toList()));
				log.info("Return value of method: {} is {}", joinPoint.getSignature().getName(), MDC.get("response"));
			} finally {
				MDC.put("response", "NotAbleToConvertToString");
				log.info("Return value of method: {} is {}", joinPoint.getSignature().getName(), MDC.get("response"));
			}
		}
		if( loggable.valueAfterReturning().isEmpty() && response != null) {
			log.info("Return value of method: {} is  {}", joinPoint.getSignature().getName(), response );
		} else {
			log.info(loggable.valueAfterReturning());
		}
		MDC.remove("method_name");
		MDC.remove("class_name");
		MDC.remove("response");
		
		
	}
	
	@Around ("logMethod()")
	public Object logAround( ProceedingJoinPoint joinPoint ) throws Throwable {
		MethodSignature methodSignature =  (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		
		long start = System.currentTimeMillis();
		
		Object result = joinPoint.proceed();
		
		long elapsedTime = System.currentTimeMillis() - start;
		
		MDC.put("execution_time_for_method_in_ms", elapsedTime);
		MDC.put("method_name", joinPoint.getSignature().getName());
		MDC.put("class_name", joinPoint.getSignature().getDeclaringTypeName());
		
		Loggable loggable = method.getAnnotation(Loggable.class);
		
		if( loggable.valueAround().isEmpty()) {
			log.info("{} completed", joinPoint.getSignature().getName().toUpperCase());
		} else {
			log.info(loggable.valueAfterReturning());
		}
		MDC.remove("method_name");
		MDC.remove("class_name");
		MDC.remove("execution_time_for_method_in_ms");
		return result;
		
	}
	
	@AfterThrowing (value = "logMethod()", throwing = "ex")
	public void logAfterThrowing( JoinPoint joinPoint, Throwable ex ) throws Throwable {
		
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
			"@annotation(logging.annotations.Loggable)"
	)
	public void logMethod() {}
}
