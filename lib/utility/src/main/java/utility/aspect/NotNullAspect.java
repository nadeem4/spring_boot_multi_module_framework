package utility.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Aspect
@Slf4j
@Configuration
public class NotNullAspect {
	@Before ("execution(* *(.., @javax.validation.constraints.NotNull (*), ..))")
	public void nullCheck( JoinPoint joinPoint ) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		for (MethodArgument argument : MethodArgument.of(joinPoint))
			if (argument.hasAnnotation(NotNull.class) && argument.getValue() == null) {
				String msg = String.format("%s: argument \"%s\" (at position %d) cannot be null",
				                           methodSignature.getMethod(),
				                           argument.getName(),
				                           argument.getIndex()
				);
				log.error(msg);
				throw new NullPointerException(msg);
			}
			
	}
	
	private static class MethodArgument {
		
		private final int index;
		private final String name;
		private final List<Annotation> annotations;
		private final Object value;
		
		private MethodArgument( int index, String name, List<Annotation> annotations, Object value ) {
			this.index = index;
			this.name = name;
			this.annotations = Collections.unmodifiableList(annotations);
			this.value = value;
		}
		
		public static List<MethodArgument> of( JoinPoint joinPoint ) {
			List<MethodArgument> arguments = new ArrayList<>();
			CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
			String[] names = codeSignature.getParameterNames();
			MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
			Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
			Object[] values = joinPoint.getArgs();
			for (int i = 0; i < values.length; i++)
				arguments.add(new MethodArgument(i, names[ i ], Arrays.asList(annotations[ i ]), values[ i ]));
			return Collections.unmodifiableList(arguments);
		}
		
		public int getIndex() { return index; }
		
		public String getName() { return name; }
		
		public List<Annotation> getAnnotations() { return annotations; }
		
		public boolean hasAnnotation( Class<? extends Annotation> type ) {
			for (Annotation annotation : annotations)
				if (annotation.annotationType().equals(type))
					return true;
			return false;
		}
		
		public Object getValue() { return value; }
		
	}
}
