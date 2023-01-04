package com.utils.spring;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.implemica.controller.service.auth.service.impl.AuthServiceImpl;

import java.util.*;

public class AuthTestUtils {

    private static final String ADMIN_USERNAME = "Admin_1";

    private static final String ADMIN_PASSWORD = "Admin_pass";

    private static final String USER_USERNAME = "User_1";

    private static final String USER_PASSWORD = "User_pass";

    private static final String secret;

    static {
        secret = AuthServiceImpl.getSecret();
    }

    public static String getAdminToken(){

        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

        return "Bearer " + JWT.create().
                withSubject(ADMIN_USERNAME)
                .withExpiresAt(new Date(System.currentTimeMillis() + 2*60*1_000))
                .withIssuer("http://localhost:8080/authorization/login")
                .withClaim("roles", List.of("ADMIN_ROLE"))
                .sign(algorithm);
    }

    public static String getAdminUsername() {
        return ADMIN_USERNAME;
    }

    public static String getAdminPassword() {
        return ADMIN_PASSWORD;
    }

    public static String getUserUsername() {
        return USER_USERNAME;
    }

    public static String getUserPassword() {
        return USER_PASSWORD;
    }

    public static boolean tokenIsValid(String token) {

        boolean isValid = true;
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
        } catch (JWTVerificationException | NullPointerException e){
            isValid = false;
        }

        return isValid;
    }
}
