package com.coffeecode.gui.controllers;

public class LocationOperationException extends RuntimeException {

    public LocationOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocationOperationException(String message) {
        super(message);
    }

}
