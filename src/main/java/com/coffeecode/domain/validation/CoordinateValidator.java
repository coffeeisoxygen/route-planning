package com.coffeecode.domain.validation;

public final class CoordinateValidator {

    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    private CoordinateValidator() {
        // Utility class
    }

    public static void validateLatitude(double latitude) {
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new ValidationException(
                    String.format("Latitude must be between %f and %f",
                            MIN_LATITUDE, MAX_LATITUDE));
        }
    }

    public static void validateLongitude(double longitude) {
        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new ValidationException(
                    String.format("Longitude must be between %f and %f",
                            MIN_LONGITUDE, MAX_LONGITUDE));
        }
    }

    public static void validate(double latitude, double longitude) {
        validateLatitude(latitude);
        validateLongitude(longitude);
    }
}
