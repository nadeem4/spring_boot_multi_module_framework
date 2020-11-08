package utility.annotations;


import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention (RetentionPolicy.RUNTIME)
public @interface AvoidDuplicate {
	
	public @NotNull String tableName();
	public @NotNull String columnName();
	public @NotNull String value();

}
