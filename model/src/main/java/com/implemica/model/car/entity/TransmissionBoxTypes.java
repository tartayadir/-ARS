package com.implemica.model.car.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;

@ApiModel
@AllArgsConstructor
public enum TransmissionBoxTypes {

    MECHANICAL("MECHANICAL"),
    AUTOMATIC("AUTOMATIC"),
    ROBOTIC("ROBOTIC"),
    VARIATIONAL("VARIATIONAL");

    private final String JsonString;

    public String getStringValue() {
        return JsonString;
    }
}

