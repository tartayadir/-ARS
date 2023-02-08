package com.implemica.controller.service.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * User authorization interface that contains basic methods for JWT token
 * generation and user information processing.
 */
public interface AuthService {

    /**
     * Process user login data and return JWT token if authorization is successful
     *
     * @param username user username
     * @param password user password
     * @return JWT token
     * @throws AuthenticationException  if authentication fails
     */
    String attemptAuthentication(String username, String password) throws AuthenticationException;

    /**
     * Creates and signs JWT token with meta user information from provided {@link Authentication}
     *
     * @param authentication {@link Authentication} object from will get principal for forming JWT token
     * @return JWT token
     */
    String successfulAuthentication(Authentication authentication);

    /**
     * Getter for secret that used for signing JWT token.
     *
     * @return secret
     */
    String getSecret();
}
