package com.utils.spring;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.implemica.controller.service.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class AuthTestUtils {

    private final AuthService authService;

    private static final String ADMIN_USERNAME = "admin";

    private static final String ADMIN_PASSWORD = "admin";

    private static final String USER_USERNAME = "admin1";

    private static final String USER_PASSWORD = "admin1";

    public String getAdminToken() {

        Algorithm algorithm = Algorithm.HMAC256(authService.getSecret().getBytes());

        return "Bearer " + JWT.create().
                withSubject(ADMIN_USERNAME)
                .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 1_000))
                .withIssuer("/authorization/login")
                .withClaim("roles", List.of("ADMIN_ROLE"))
                .sign(algorithm);
    }

    public static String getFirstAdminUsername() {
        return ADMIN_USERNAME;
    }

    public static String getFirstAdminPassword() {
        return ADMIN_PASSWORD;
    }

    public static String getSecondAdminUsername() {
        return USER_USERNAME;
    }

    public static String getSecondAdminPassword() {
        return USER_PASSWORD;
    }

    public boolean tokenIsValid(String token) {

        boolean isValid = true;
        try {

            Algorithm algorithm = Algorithm.HMAC256(authService.getSecret().getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
        } catch (JWTVerificationException | NullPointerException e) {
            isValid = false;
        }

        return isValid;
    }
}
