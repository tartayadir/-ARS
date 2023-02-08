package com.implemica.controller.exceptions;

/**
 * Exception to be thrown when validate images in
 * {@link com.implemica.controller.service.amazonS3.AmazonClient} service.
 */
public class InvalidImageTypeException extends Exception{

    /**
     * Constructs a new exception with the specified detail message.
     */
    public InvalidImageTypeException(String message) {
        super(message);
    }
}
