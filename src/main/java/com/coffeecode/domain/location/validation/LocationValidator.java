package com.coffeecode.domain.location.validation;

/*
 * this class is Lovation Validation
 */
import com.coffeecode.domain.exception.InvalidLocationException;

public final class LocationValidator {

    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    private LocationValidator() {
    }

    public static void validateAll(String name, double latitude, double longitude) {
        validateName(name);
        validateCoordinates(latitude, longitude);
    }

    public static void validateCoordinates(double latitude, double longitude) {
        validateLatitude(latitude);
        validateLongitude(longitude);
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidLocationException("Name cannot be null or empty");
        }
    }

    private static void validateLatitude(double latitude) {
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new InvalidLocationException(
                    String.format("Latitude must be between %f and %f", MIN_LATITUDE, MAX_LATITUDE));
        }
    }

    private static void validateLongitude(double longitude) {
        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new InvalidLocationException(
                    String.format("Longitude must be between %f and %f", MIN_LONGITUDE, MAX_LONGITUDE));
        }
    }
}
