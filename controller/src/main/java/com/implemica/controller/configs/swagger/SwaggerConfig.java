package com.implemica.controller.configs.swagger;

import com.beust.jcommander.internal.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * Provides configuration for Swagger and set necessary values by methods, that
 * set up logic for working with JWT token to performing authorized requests. Also,
 * provides information: description, contacts and other; about application to Swagger.
 *
 */
@Configuration
@EnableWebMvc
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    /**
     * Http header name where authorization JWT token will be set
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Provides {@link Docket} with Swagger information and settings.
     *
     * @return object with settings and meta information
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Provides the api's meta information about application, API version and contacts
     *
     * @return {@link ApiInfo} object with meta information
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Car catalog V1.0")
                .description("This is API for work with the car catalog server. For this sample, you can use the JWT" +
                        " to test the authorization filters and perform authorized requests to server like add or" +
                        "delete car. You should put your JWT token with prefix \"Bearer \" in authorise filed to " +
                        "authorize. Also, there are examples of request and response that you can use for test API.\n")
                .contact(new Contact("Implemica", "https://implemica.com", "welcome@implemica.com"))
                .version("1.0.0")
                .build();
    }

    /**
     * Provides class with settings that applicable to all or some of the api operations.
     *
     * @return {@link ApiKey} object with api keys settings
     */
    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_HEADER,AUTHORIZATION_HEADER, "header");
    }

    /**
     * Provides configuration which api operations (via regex patterns) and
     * HTTP methods to apply security contexts to apis.
     *
     * @return security contexts
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    /**
     * Provides list of {@link SecurityReference}
     *
     * @return {@link SecurityReference} list
     */
    private List<SecurityReference> defaultAuth() {

        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        return List.of(new SecurityReference(AUTHORIZATION_HEADER, new AuthorizationScope[]{authorizationScope}));
    }
}