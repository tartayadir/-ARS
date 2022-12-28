package com.implemica.model.car.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@ApiModel
@AllArgsConstructor
public enum CarBodyTypes {

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

    private static final List<CarBodyTypes> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static CarBodyTypes getRandomCarBodyType()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}