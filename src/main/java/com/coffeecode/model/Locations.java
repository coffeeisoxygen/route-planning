package com.coffeecode.model;

import java.util.UUID;

public record Locations(UUID id, String name, double latitude, double longitude) {

    public Locations(String name, double latitude, double longitude) {
        this(UUID.randomUUID(), name, latitude, longitude);
    }

    public Locations {
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

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return rad * (180.0 / Math.PI);
    }

    public double distanceTo(Locations other) {
        if (other == null) {
            throw new IllegalArgumentException("Other location cannot be null");
        }

        double theta = this.longitude - other.longitude;
        double dist = Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(other.latitude))
                + Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(other.latitude))
                * Math.cos(deg2rad(theta));

        dist = Math.acos(Math.min(dist, 1.0)); // Prevent domain error
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344; // Convert to kilometers
        if (Double.isNaN(dist)) {
            dist = 0.0;
        }

        return dist;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) at %.6f, %.6f",
                name, id.toString().substring(0, 8), latitude, longitude);
    }
}
