package apidocs.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;


@Configuration
public class SwaggerConfig {
	
	@Value ("${app.swagger.oauth.client-id:}")
	private String clientId;
	
	@Value("${app.swagger.oauth.client-key:}")
	private String clientSecret;
	
	@Value("${app.swagger.oauth.login-endpoint:}")
	private String loginEndpointUrl;
	
	@Value("${app.swagger.oauth.resource:}")
	private String resource;
	
	@Value("${app.swagger.enable-auth:false}")
	private Boolean enableAuth;
	
	@Value("${app.swagger.title}")
	private String title;
	
	@Value("${app.swagger.desc}")
	private String desc;
	
	@Value("${app.swagger.version}")
	private String version;
	
	@Value("${app.swagger.path-mapping}")
	private String pathMapping;

	@Value("${app.swagger.contact.name:Backend Team}")
	private String contactName;

	@Value("${app.swagger.contact.email:}")
	private String contactEmail;

	@Value("${app.swagger.contact.url:}")
	private String companyUrl;

	@Value("${app.swagger.license:null}")
	private String license;

	@Value("${app.swagger.license-uri:null}")
	private String licenseUrl;
	
	private ApiInfo apiDetails() {
		return new ApiInfo(
				title,
				desc,
				version,
				null,
				new springfox.documentation.service.Contact(
						contactName,
						companyUrl,
						contactEmail
				),
				license,
				licenseUrl,
				Collections.emptyList()
		);
	}
	
	
	@Bean
	public Docket swaggerConfig() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiDetails())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/v1/*"))
				.build()
				.pathMapping(pathMapping)
				.securitySchemes( enableAuth ? Collections.singletonList(securitySchema()): Collections.EMPTY_LIST)
				.securityContexts(enableAuth ?  Collections.singletonList(securityContext()): Collections.EMPTY_LIST);
	}
	
	
	@Bean
	UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder()
				.deepLinking(true)
				.displayOperationId(false)
				.defaultModelsExpandDepth(1)
				.defaultModelExpandDepth(1)
				.defaultModelRendering(ModelRendering.EXAMPLE)
				.displayRequestDuration(false)
				.docExpansion(DocExpansion.NONE)
				.filter(true)
				.maxDisplayedTags(null)
				.operationsSorter(OperationsSorter.ALPHA)
				.showExtensions(false)
				.tagsSorter(TagsSorter.ALPHA)
				.supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
				.validatorUrl(null)
				.build();
	}
	
	private OAuth securitySchema() {
		List<AuthorizationScope> authorizationScopeList = newArrayList();
		authorizationScopeList.add(new AuthorizationScope("read", "read all"));
		authorizationScopeList.add(new AuthorizationScope("trust", "trust all"));
		authorizationScopeList.add(new AuthorizationScope("write", "access all"));
		
		List<GrantType> grantTypes = newArrayList();
		LoginEndpoint loginEndpoint = new LoginEndpoint(loginEndpointUrl);
		GrantType creGrant = new ImplicitGrant( loginEndpoint, "access_token" );
		
		grantTypes.add(creGrant);
		
		return new OAuth("oauth2schema", authorizationScopeList, grantTypes);
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.build();
	}
	
	private List<SecurityReference> defaultAuth() {
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
		authorizationScopes[0] = new AuthorizationScope("read", "read all");
		authorizationScopes[1] = new AuthorizationScope("trust", "trust all");
		authorizationScopes[2] = new AuthorizationScope("write", "write all");
		
		return Collections.singletonList(new SecurityReference("oauth2schema", authorizationScopes));
	}
	
	@Bean
	public SecurityConfiguration securityInfo() {
		Map<String, Object> queryParam = new HashMap<>();
		queryParam.put("resource", resource);
		return SecurityConfigurationBuilder.builder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.appName("client_app")
				.scopeSeparator(",")
				.additionalQueryStringParams(queryParam)
				.useBasicAuthenticationWithAccessCodeGrant(false)
				.enableCsrfSupport(false)
				.build();
	}
	
	
}
