package com.implemica.controller.configs.security;

import com.implemica.controller.configs.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private static final String ADMIN_ROLE_NAME = "ADMIN_ROLE";

    private static final String LOGIN_URI = "/authorization/login";

    private static final String IMAGE_URI = "/image/*";

    private static final String CAR_URI = "/car-catalog/*";

    private static final String CAR_BASE_URI = "/car-catalog";

    private static final String[] SWAGGER_AUTH_WHITELIST = {
            "/swagger-ui/index.html",
            "/authenticate",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui/",
            "/swagger-ui",
            "/javainuse-openapi/**",
            "/v2/api-docs/**",
            "/swagger.json",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Value("${cars.token.secret}")
    private String secret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.addAllowedMethod(DELETE);
        configuration.addAllowedMethod(PUT);

        http.csrf().disable();
        http.cors().configurationSource(request -> configuration);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers(SWAGGER_AUTH_WHITELIST).permitAll();
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
