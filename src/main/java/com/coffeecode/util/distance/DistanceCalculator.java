package com.coffeecode.util.distance;

public interface DistanceCalculator {

    double calculate(double lat1, double lon1, double lat2, double lon2);

    String getStrategyName();

}
