package com.coffeecode.domain.model;

import java.util.UUID;
import com.coffeecode.domain.exception.InvalidLocationException;

public record Locations(UUID id, String name, double latitude, double longitude) {

    public Locations    {
        validate(name, latitude, longitude);
    }

    public Locations(String name, double latitude, double longitude) {
        this(UUID.randomUUID(), name, latitude, longitude);
    }

    private static void validate(String name, double latitude, double longitude) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidLocationException("Name cannot be null or empty");
        }
        if (latitude < -90 || latitude > 90) {
            throw new InvalidLocationException("latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new InvalidLocationException("longitude must be between -180 and 180");
        }
    }

    /**
     * Simple spherical distance calculation. For high-precision calculations,
     * use infrastructure layer.
     */
    public double distanceTo(double lat, double lon) {
        double earthRadiusInKM = 6371; // Earth radius in kilometers
        double dLat = Math.toRadians(lat - this.latitude);
        double dLon = Math.toRadians(lon - this.longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(this.latitude))
                * Math.cos(Math.toRadians(lat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusInKM * c;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) at %.6f, %.6f",
                name, id.toString().substring(0, 8), latitude, longitude);
    }
}
