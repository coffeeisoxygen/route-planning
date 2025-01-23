package com.coffeecode.domain.location.model;

/*
 * This Class Is Data Structure For Locations
 */
import java.util.UUID;

import com.coffeecode.domain.location.validation.LocationValidator;

public record Locations(UUID id, String name, double latitude, double longitude) {

    public Locations    {
        LocationValidator.validateName(name);
        LocationValidator.validateCoordinates(latitude, longitude);
    }

    public Locations(String name, double latitude, double longitude) {
        this(UUID.randomUUID(), name, latitude, longitude);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) at %.6f, %.6f",
                name, id.toString().substring(0, 8), latitude, longitude);
    }
}
