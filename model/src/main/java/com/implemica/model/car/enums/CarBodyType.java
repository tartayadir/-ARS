package com.implemica.model.car.enums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Random;

@ApiModel
@AllArgsConstructor
public enum CarBodyType {

    SEDAN("Sedan"),
    COUPE("Coupe"),
    SPORTS_CAR("Sports car"),
    STATION_WAGON("Station wagon"),
    HATCHBACK("Hatchback"),
    CONVERTIBLE("Convertible"),
    MINIVAN("Minivan"),
    PICKUP("Pickup"),
    SUV("SUV");

    private final String JsonString;

    public String getStringValue() {
        return JsonString;
    }
}