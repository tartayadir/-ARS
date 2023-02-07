package com.implemica.controller.configs.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private static final String LOGIN_URI = "/authorization/login";

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String ROLES_CLAIM_NAME = "roles";

    private final String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals(LOGIN_URI)) {
            filterChain.doFilter(request, response);

        } else {

            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {

                SecurityContextHolder.getContext().setAuthentication(getAuthenticationToken(authorizationHeader));
                filterChain.doFilter(request, response);

            } else {
                filterChain.doFilter(request, response);
            }

        }
    }

    private DecodedJWT verifyToken(String authorizationHeader) {

        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();

        return verifier.verify(token);
    }

    private static Collection<SimpleGrantedAuthority> getRoles(DecodedJWT decodedJWT) {

        String[] roles = decodedJWT.getClaim(ROLES_CLAIM_NAME).asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        return authorities;
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String authorizationHeader) {

        DecodedJWT decodedJWT = verifyToken(authorizationHeader);
        String username = decodedJWT.getSubject();
        Collection<SimpleGrantedAuthority> authorities = getRoles(decodedJWT);

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
