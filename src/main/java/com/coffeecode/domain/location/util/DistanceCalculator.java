package com.coffeecode.domain.location.util;

import com.coffeecode.domain.location.model.Locations;

public interface DistanceCalculator {

    double calculateDistance(double fromLat, double fromLon, double toLat, double toLon);

    default double calculateDistance(Locations from, Locations to) {
        return calculateDistance(
                from.latitude(), from.longitude(),
                to.latitude(), to.longitude()
        );
    }
}
