package com.implemica.model.car.enums;

import com.implemica.model.car.entity.Car;
import lombok.AllArgsConstructor;

/**
 * The thing is the type of car transmission box contains a field which is a standard entry
 * of the name of the transmission box type. This is used in {@link Car} for typing.
 */
@AllArgsConstructor
public enum TransmissionBoxType {

    MECHANICAL("MECHANICAL"),
    AUTOMATIC("AUTOMATIC"),
    ROBOTIC("ROBOTIC"),
    VARIATIONAL("VARIATIONAL");

    /**
     * Standard entry for transmission box type name.
     */
    private final String standardName;

    /**
     * Getter for standard entry.
     *
     * @return standard entry of car transmission box type
     */
    public String getStringValue() {
        return standardName;
    }
}

