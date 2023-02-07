package com.implemica.controller.service.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthService {

    String attemptAuthentication(String username, String password) throws AuthenticationException;

    String successfulAuthentication(Authentication authentication);

    String getSecret();
}
