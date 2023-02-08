package com.implemica.controller.exceptions;

/**
 * Exception to be thrown when cannot find {@link com.implemica.model.car.entity.Car} in database
 */
public class NoSuchCarException extends Exception{

    /**
     * Constructs a new exception with the specified detail message.
     */
    public NoSuchCarException(String message) {
        super(message);
    }
}
