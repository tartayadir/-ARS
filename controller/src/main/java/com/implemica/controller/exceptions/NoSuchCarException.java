package com.implemica.controller.exceptions;

import com.implemica.model.car.entity.Car;

/**
 * Exception to be thrown when cannot find {@link Car} entity in database.
 */
public class NoSuchCarException extends Exception{

    /**
     * Constructs a new exception with the specified detail message.
     */
    public NoSuchCarException(String message) {
        super(message);
    }
}
