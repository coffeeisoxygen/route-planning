package com.coffeecode.domain.model;

import java.util.UUID;

import com.coffeecode.domain.exception.InvalidLocationException;
import com.coffeecode.domain.validation.CoordinateValidator;

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
        CoordinateValidator.validate(latitude, longitude);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) at %.6f, %.6f",
                name, id.toString().substring(0, 8), latitude, longitude);
    }
}
