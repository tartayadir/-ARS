package com.implemica.model.car.enums;

import com.implemica.model.car.entity.Car;
import lombok.AllArgsConstructor;

/**
 * The thing is the type of car body contains a field which is a standard entry
 * of the name of the body. This is used in {@link Car} for typing.
 */
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

    /**
     * Standard entry for body type name.
     */
    private final String standardName;

    /**
     * Getter for standard entry.
     *
     * @return standard entry of car body type
     */
    public String getStringValue() {
        return standardName;
    }
}