package utility.aspect;

import exception.custom.DuplicateRecordException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.FieldSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import utility.annotations.AvoidDuplicate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
@Slf4j
@Configuration
@Component
public class AvoidDuplicateAspect {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleJdbcCall simpleJdbcCall;
	
	@Before("avoidDuplicate()")
	public void runBefore( JoinPoint joinPoint ) {
		FieldSignature methodSignature =  (FieldSignature) joinPoint.getSignature();
		Field field = methodSignature.getField();
		AvoidDuplicate avoidDuplicate = field.getDeclaredAnnotation(AvoidDuplicate.class);
		
		String sqlQuery = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", avoidDuplicate.tableName(), avoidDuplicate.columnName());
		Integer cnt = jdbcTemplate.queryForObject(
				sqlQuery,
				new Object[] {avoidDuplicate.value()},
				Integer.class
		);
		if( cnt > 0) {
			MDC.put("table_name", avoidDuplicate.tableName());
			MDC.put("column_name", avoidDuplicate.columnName());
			MDC.put("value", avoidDuplicate.value());
			log.error("{}: {} already exist in {}", avoidDuplicate.columnName(), field, avoidDuplicate.tableName());
			throw new DuplicateRecordException(avoidDuplicate.columnName(), avoidDuplicate.value());
		}
	}
	
	@Pointcut (
			"@annotation(utility.annotations.AvoidDuplicate)"
	)
	public void avoidDuplicate() {}
}
