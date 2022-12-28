package com.implemica.controller.service.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.implemica.controller.service.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private static final String secret = "MegaLargeSigningSecretKeyForDemoApplicationMegaLargeSigningSecretKeyForDemoApplica" +
            "tion606f95011b64545d269fc6e7286c289f8c7a164bd429be382ba6d87c";

    @Override
    public String attemptAuthentication(String username, String password) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        authenticationToken.getAuthorities().forEach(System.out::println);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        return this.successfulAuthentication(authentication);
    }

    @Override
    public String successfulAuthentication(Authentication authentication) {

        User user = (User)authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create().
                withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30*60*1_000))
                .withIssuer("/api/login")
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).
                        collect(Collectors.toList()))
                .sign(algorithm);
    }
     
    public static String getSecret() {
        return secret;
    }

}
