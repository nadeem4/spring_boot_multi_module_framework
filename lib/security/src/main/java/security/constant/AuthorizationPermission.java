package security.constant;

public class AuthorizationPermission {
	
	public static final String SERVICE_CENTER_ADMIN = "hasAnyRole('COPO_Service_Center_Administrator')";
	public static final String OUTSIDE_PROCESSOR_ADMIN = "hasAnyRole('COPO_Outside_Processing_Administrator')";
	public static final String APP_ADMIN = "hasAnyRole('COPO_App_Administrator')";
	public static final String SERVICE_CENTER_AND_OUTSIDE_PROCESSOR_ADMIN = "hasAnyRole('COPO_Service_Center_Administrator', 'COPO_Outside_Processing_Administrator')";
	public static final String SERVICE_CENTER_AND_APP_ADMIN = "hasAnyRole('COPO_Service_Center_Administrator', 'COPO_App_Administrator')";
	public static final String OUTSIDE_PROCESSOR_AND_APP_ADMIN = "hasAnyRole('COPO_App_Administrator', 'COPO_Outside_Processing_Administrator')";
	public static final String SERVICE_CENTER_OUTSIDE_PROCESSOR_AND_APP_ADMIN = "hasAnyRole('COPO_Service_Center_Administrator', 'COPO_App_Administrator', 'COPO_Outside_Processing_Administrator')";
}
