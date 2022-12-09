package com.implemica.controller.controllers;

import com.implemica.controller.service.auth.service.AuthService;
import com.implemica.model.auth.AuthResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"Authorization API"}, description = "Api section for Authorization service")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Get token by login and password.",
            description = "Get a token to work with the API by login and password of the registered user")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful authorization.",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Failed authorization.",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE)})})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> login(@RequestParam @Parameter(description = "User username") String username,
                                              @RequestParam @Parameter(description = "User password") String password){

        log.info("Http method - Post, try to login user with name{}", username);

        String token = authService.attemptAuthentication(username, password);
        AuthResponse authResponse = new AuthResponse(token);

        return ResponseEntity.ok().body(authResponse);
    }
}
