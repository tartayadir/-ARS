package com.implemica.controller.configs.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * A filter class whose purpose is to ensure that a single sending request is executed
 * in any servlet container. It provides the method {@link #doFilterInternal} with
 * HttpServletRequest and HttpServletResponse arguments. Processes an authentication
 * form submission. Also provides several necessary beans for work with security.
 */
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    /**
     * URI of login endpoint
     */
    private static final String LOGIN_URI = "/authorization/login";

    /**
     * JWT token prefix in authorization header
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Name of claim with roles in payload part int JWT
     */
    private static final String ROLES_CLAIM_NAME = "roles";

    /**
     * The secret for verifying JWT token
     */
    private final String secret;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request The servlet container creates an <code>HttpServletRequest</code> object and
     *                passes it as an argument to the servlet's service methods
     *                (<code>doGet</code>, <code>doPost</code>, etc). From it are got JWT token
     * @param response The servlet container creates an <code>HttpServletResponse</code> object and
     *                 passes it as an argument to the servlet's service methods (<code>doGet</code>,
     *                 <code>doPost</code>, etc).
     * @param filterChain filters through which the request will pass
     * @throws IOException if an I/O error occurs during the processing of the
     *                     request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

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

    /**
     * Performs the verification against the given Token, using any previous configured options.
     *
     * @param authorizationHeader its token with {@link #TOKEN_PREFIX} form authorization header.
     * @return a verified and decoded JWT.
     * @throws AlgorithmMismatchException     if the algorithm stated in the token's header it's not equal to the one defined in the {@link JWTVerifier}.
     * @throws SignatureVerificationException if the signature is invalid.
     * @throws TokenExpiredException          if the token has expired.
     * @throws InvalidClaimException          if a claim contained a different value than the expected one.
     */
    private DecodedJWT verifyToken(String authorizationHeader) {

        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();

        return verifier.verify(token);
    }

    /**
     * Retrieves a list of user roles from the token and returns a list
     * of roles as a list of objects of type {@link SimpleGrantedAuthority}
     *
     * @param decodedJWT JWT token with roles
     * @return list of user roles
     */
    private static Collection<SimpleGrantedAuthority> getRoles(DecodedJWT decodedJWT) {

        String[] roles = decodedJWT.getClaim(ROLES_CLAIM_NAME).asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        return authorities;
    }

    /**
     * Verifies the token and if the token is valid, pulls out the name and role
     * of the user and forms {@link UsernamePasswordAuthenticationToken} for simple
     * presentation of a username and password.
     *
     * @param authorizationHeader its token with {@link #TOKEN_PREFIX} form authorization header
     * @return presentation of user with her name and roles
     */
    private UsernamePasswordAuthenticationToken getAuthenticationToken(String authorizationHeader) {

        DecodedJWT decodedJWT = verifyToken(authorizationHeader);
        String username = decodedJWT.getSubject();
        Collection<SimpleGrantedAuthority> authorities = getRoles(decodedJWT);

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
