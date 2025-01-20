package com.coffeecode.model;

import java.util.UUID;

public record Locations(UUID id, String name, double latitude, double longitude) {

    public Locations(String name, double latitude, double longitude) {
        this(UUID.randomUUID(), name, latitude, longitude);
    }

    public Locations    {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) at %.6f, %.6f",
                name, id.toString().substring(0, 8), latitude, longitude);
    }
}
