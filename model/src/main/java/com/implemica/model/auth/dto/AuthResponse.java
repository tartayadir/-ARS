package com.implemica.model.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Response DTO for successful authorization login request that contains JWT token
 */
@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {

    @ApiModelProperty(name = "Access JWT token", notes = "JWT that is used for performing authorized requests.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private String access_token;
}
