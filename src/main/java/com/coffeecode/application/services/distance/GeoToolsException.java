package com.coffeecode.application.services.distance;

public class GeoToolsException extends RuntimeException {

    public GeoToolsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeoToolsException(String message) {
        super(message);
    }

}
