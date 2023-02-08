package com.implemica.model.error_massage;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Response DTO that will return when exception handler catch
 * exception. It contains message about error.
 */
@Getter
@AllArgsConstructor
@ApiModel("ErrorMassageResponse")
public class ErrorMassageResponse implements Serializable {

    /**
     * It is error message that inform about causes of it
     * and other information.
     */
    private String error_message;
}
