package com.implemica.controller.exceptions;

import com.implemica.controller.service.amazonS3.AmazonClient;

/**
 * Exception to be thrown when validate images files before
 * uploading to S3 bucket and is used in {@link AmazonClient} service.
 */
public class InvalidImageTypeException extends Exception{

    /**
     * Constructs a new exception with the specified detail message.
     */
    public InvalidImageTypeException(String message) {
        super(message);
    }
}
