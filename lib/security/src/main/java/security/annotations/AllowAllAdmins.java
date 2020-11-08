package security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;
import security.constant.AuthorizationPermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@PreAuthorize (AuthorizationPermission.SERVICE_CENTER_OUTSIDE_PROCESSOR_AND_APP_ADMIN)
@Target ({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention (RetentionPolicy.RUNTIME)
public @interface AllowAllAdmins {
}
