package utils.spring;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.implemica.controller.service.auth.service.impl.AuthServiceImpl;

import java.util.*;

public class AuthTestUtils {

    private static final String username = "Admin_1";

    private static final String password = "a43eb61ea0d7cfc3a56b71c64c848be7c9834ff33dcf2d2d21d5b06a16a98b36";

    private static String secret;

    static {
        secret = AuthServiceImpl.getSecret();
    }

    public static String getToken(){

        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

        return "Bearer " + JWT.create().
                withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 2*60*1_000))
                .withIssuer("http://localhost:8080/api/login")
                .withClaim("roles", List.of("ADMIN_ROLE"))
                .sign(algorithm);
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
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
