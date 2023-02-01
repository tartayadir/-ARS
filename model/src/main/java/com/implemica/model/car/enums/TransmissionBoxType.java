package com.implemica.model.car.enums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;

@ApiModel
@AllArgsConstructor
public enum TransmissionBoxType {

    MECHANICAL("MECHANICAL"),
    AUTOMATIC("AUTOMATIC"),
    ROBOTIC("ROBOTIC"),
    VARIATIONAL("VARIATIONAL");

    private final String JsonString;

    public String getStringValue() {
        return JsonString;
    }
}

