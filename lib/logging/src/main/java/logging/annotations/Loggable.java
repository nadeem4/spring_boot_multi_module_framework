package logging.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target ({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention (RetentionPolicy.RUNTIME)
public @interface Loggable {
	
	public String valueBefore() default  "";
	public String valueAfter() default  "";
	public String valueAfterReturning() default  "";
	public String valueAround() default  "";
}
