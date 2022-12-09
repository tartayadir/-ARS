package com.implemica.model.car.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;

@ApiModel
@AllArgsConstructor
public enum CarBodyTypes {

    SEDAN("SEDAN"),
    COUPE("COUPE"),
    SPORTS_CAR("SPORTS_CAR"),
    STATION_WAGON("STATION_WAGON"),
    HATCHBACK("HATCHBACK"),
    CONVERTIBLE("CONVERTIBLE"),
    MINIVAN("MINIVAN"),
    PICKUP("PICKUP"),
    SUV("SUV");

    private final String JsonString;

    public String getStringValue() {
        return JsonString;
    }
}