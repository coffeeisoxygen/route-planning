package com.coffeecode.domain.util;

import com.coffeecode.domain.model.Locations;

public interface DistanceCalculator {

    double calculateDistance(double fromLat, double fromLon, double toLat, double toLon);

    default double calculateDistance(Locations from, Locations to) {
        return calculateDistance(
                from.latitude(), from.longitude(),
                to.latitude(), to.longitude()
        );
    }
}
