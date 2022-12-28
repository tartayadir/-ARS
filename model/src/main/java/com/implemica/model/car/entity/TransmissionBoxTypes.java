package com.implemica.model.car.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Random;

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

    private static final List<TransmissionBoxTypes> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static TransmissionBoxTypes getRandomTransmissionBoxTypes()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}

