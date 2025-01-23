package com.coffeecode.domain.location.service;

import java.util.UUID;

import com.coffeecode.domain.location.model.Locations;
import com.coffeecode.domain.location.util.DistanceCalculator;

public class LocationOperations {

    private final LocationMap locationMap;
    private final DistanceCalculator calculator;

    public LocationOperations(LocationMap locationMap, DistanceCalculator calculator) {
        this.locationMap = locationMap;
        this.calculator = calculator;
    }

    public double calculateDistance(UUID source, UUID target) {
        Locations sourceLocation = locationMap.getLocation(source)
                .orElseThrow(() -> new IllegalArgumentException("Source location not found: " + source));
        Locations targetLocation = locationMap.getLocation(target)
                .orElseThrow(() -> new IllegalArgumentException("Target location not found: " + target));

        return calculator.calculateDistance(sourceLocation, targetLocation);
    }
}
