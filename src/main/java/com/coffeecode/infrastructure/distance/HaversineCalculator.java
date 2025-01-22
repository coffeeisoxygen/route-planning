package com.coffeecode.infrastructure.distance;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.exception.InvalidLocationException;
import com.coffeecode.domain.util.DistanceCalculator;
import com.coffeecode.domain.validation.CoordinateValidator;

@Component
public class HaversineCalculator implements DistanceCalculator {

    private static final double EARTH_RADIUS = 6371; // kilometers

    @Override
    public double calculateDistance(double fromLat, double fromLon, double toLat, double toLon) {
        try {
            CoordinateValidator.validate(fromLat, fromLon);
            CoordinateValidator.validate(toLat, toLon);
        } catch (InvalidLocationException e) {
            throw new DistanceCalculatorException("Invalid coordinates", e);
        }

        double dLat = Math.toRadians(toLat - fromLat);
        double dLon = Math.toRadians(toLon - fromLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(fromLat))
                * Math.cos(Math.toRadians(toLat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
