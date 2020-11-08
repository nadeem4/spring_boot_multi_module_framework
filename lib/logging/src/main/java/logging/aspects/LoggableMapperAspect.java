package logging.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggableMapperAspect {
	
	@Before ("execution(*.mapper.*)")
	public void logBefore( JoinPoint joinPoint ) throws Throwable {
	
	}
	
	@After ("execution(*.mapper.*)")
	public void logAfter( JoinPoint joinPoint ) throws Throwable {
	
	}
	
	
	@AfterReturning ("execution(*.mapper.*)")
	public void logAfterReturning( JoinPoint joinPoint ) throws Throwable {
	
	}
}
