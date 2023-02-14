package com.implemica.model.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("AuthRequest")
public class AuthRequest implements Serializable {

    @ApiModelProperty(notes = "The username that is used for authorization.", example = "username")
    @NotEmpty(message = "Username is required.")
    @Size(min = 2, max = 31, message = "Username must be greater than 1 and less than 30.")
    @Pattern(regexp = "^[a-zA-z0-9_ ]+", message = "Username must not contain anything other than digits," +
            " letters and underlining.")
    private String username;

    @ApiModelProperty(notes = "The username that is used for authorization.", example = "password")
    @NotEmpty(message = "Password is required.")
    private String password;
}
