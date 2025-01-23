package com.coffeecode.domain.location.exception;

public class DistanceCalculatorException extends RuntimeException {

    public DistanceCalculatorException(String message) {
        super(message);
    }

    public DistanceCalculatorException(String message, Throwable cause) {
        super(message, cause);
    }

}
