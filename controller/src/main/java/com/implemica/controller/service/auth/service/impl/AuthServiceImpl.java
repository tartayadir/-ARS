package com.implemica.controller.service.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.implemica.controller.service.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * Implements {@link AuthService} interface for user authorization and token creation.
 * To sign tokens, use the given secret from the file. Also use the provided {@link AuthenticationManager}
 * from the {@link com.implemica.controller.configs.security.SecurityConfig} config class.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    /**
     * Uses for do authenticate for login user data
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Secret used to sign tokens
     */
    @Value("${cars.JWT.secret}")
    private String secret;

    /**
     * Expiration JWT token time
     */
    @Value("${cars.JWT.token-expiration-time}")
    private long expirationTokenTime;

    /**
     * Implements {@link AuthService#attemptAuthentication(String, String)} with using {@link AuthenticationManager}
     * from the {@link com.implemica.controller.configs.security.SecurityConfig} config class.
     *
     * @param username user username
     * @param password user password
     * @return JWT token
     * @throws AuthenticationException  if authentication fails
     */
    @Override
    public String attemptAuthentication(String username, String password) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username.toLowerCase(), password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        return this.successfulAuthentication(authentication);
    }

    /**
     * Implements {@link AuthService#successfulAuthentication(Authentication)} that creates JWT token
     * with using provided secret and expiration time
     *
     * @param authentication {@link Authentication} object from will get principal for forming JWT token
     * @return JWT token
     */
    @Override
    public String successfulAuthentication(Authentication authentication) {

        User user = (User)authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create().
                withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTokenTime))
                .withIssuer("/authorization/login")
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).
                        collect(Collectors.toList()))
                .sign(algorithm);
    }

    /**
     * Impliments {@link AuthService#getSecret()} getter for secret that used for
     * signing JWT token.
     *
     * @return secret
     */
    @Override
    public String getSecret() {
        return secret;
    }
}
