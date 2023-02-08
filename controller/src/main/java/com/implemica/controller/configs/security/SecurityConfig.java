package com.implemica.controller.configs.security;

import com.implemica.controller.configs.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.http.HttpMethod.*;

/**
 * Security configuration that create and provide necessary  beans: {@link PasswordEncoder},
 * {@link AuthenticationManager} and {@link SecurityFilterChain}. Provide custom {@link UserDetailsService} and
 * secret for verifying JWT tokens to request filter, set up CORS and http security.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Service for work with users that will be provided to request filter
     */
    private final UserDetailsService userDetailsService;

    /**
     * Name of admin role that provide for permitting access to authorized requests
     */
    private static final String ADMIN_ROLE_NAME = "ADMIN_ROLE";

    /**
     * Login endpoint UIR
     */
    private static final String LOGIN_URI = "/authorization/login";

    /**
     * Image API base URI
     */
    private static final String IMAGE_URI = "/image/*";

    /**
     * Car API base URI
     */
    private static final String CAR_URI = "/car-catalog/*";

    /**
     * Car API URI
     */
    private static final String CAR_BASE_URI = "/car-catalog";

    /**
     * URI for access from Swagger
     */
    private static final String SWAGGER_UI_URI = "/swagger-ui/**";

    /**
     * Secret that is provided to request filter for verifying JWT tokens
     */
    @Value("${cars.JWT.secret}")
    private String secret;

    /**
     * Provides password encoder bean for hashing passwords
     *
     * @return password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides authentication manager that processes an authentication request with
     * provided password encoder and user details service.
     *
     * @param http configured web based security for specific http requests
     * @return authentication manager with provided password encoder and user details service
     * @throws Exception if an error occurs when adding the {@link UserDetailsService}
      * based authentication or an error occurred when building the Object
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    /**
     * Sets up CORS configuration, http web security and adds authorization request filter.
     *
     * @param http configured web based security for specific http requests
     * @return filter chain which is capable of being matched against an HttpServletRequest with
     *         custom CORS, authorization filter and http security configuration.
     * @throws Exception if an error occurred when building the Object
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.addAllowedMethod(DELETE);
        configuration.addAllowedMethod(PUT);

        http.csrf().disable();
        http.cors().configurationSource(request -> configuration);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers(SWAGGER_UI_URI).permitAll();
        http.authorizeRequests().antMatchers(GET, CAR_URI, CAR_BASE_URI).permitAll();
        http.authorizeRequests().antMatchers(POST, LOGIN_URI).permitAll();
        http.authorizeRequests().antMatchers(POST, CAR_BASE_URI, IMAGE_URI).hasAnyAuthority(ADMIN_ROLE_NAME);
        http.authorizeRequests().antMatchers(PUT, CAR_BASE_URI).hasAnyAuthority(ADMIN_ROLE_NAME);
        http.authorizeRequests().antMatchers(DELETE, CAR_URI, IMAGE_URI).hasAnyAuthority(ADMIN_ROLE_NAME);

        http.authorizeRequests().anyRequest().permitAll();
        http.addFilterBefore(new CustomAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
