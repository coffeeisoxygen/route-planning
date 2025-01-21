package com.coffeecode.domain.model;

import java.util.UUID;

public record Locations(UUID id, String name, double latitude, double longitude) {

    public Locations    {
        validate(name, latitude, longitude);
    }

    public Locations(String name, double latitude, double longitude) {
        this(UUID.randomUUID(), name, latitude, longitude);
    }

    private static void validate(String name, double latitude, double longitude) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("longitude must be between -180 and 180");
        }
    }

    public double distanceTo(double lat, double lon) {
        return Math.sqrt(
                Math.pow(latitude - lat, 2)
                + Math.pow(longitude - lon, 2)
        );
    }

    @Override
    public String toString() {
        return String.format("%s (%s) at %.6f, %.6f",
                name, id.toString().substring(0, 8), latitude, longitude);
    }
}
