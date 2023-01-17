package com.implemica.model.error_massage;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@ApiModel("ErrorMassageResponse")
public class ErrorMassageResponse implements Serializable {

    private String error_message;
}
