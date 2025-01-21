package com.coffeecode.infrastructure.distance;

public class GeoToolsException extends RuntimeException {

    public GeoToolsException(String message) {
        super(message);
    }

    public GeoToolsException(String message, Throwable cause) {
        super(message, cause);
    }

}
