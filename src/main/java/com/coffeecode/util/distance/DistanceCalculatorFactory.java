package com.coffeecode.util.distance;

public class DistanceCalculatorFactory {

    private DistanceCalculatorFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static DistanceCalculator getDistanceCalculator(DistanceCalulatorName name) {
        if (name == DistanceCalulatorName.GEOTOOLS) {
            return new GeoToolsCalculator();
        } else {
            throw new IllegalArgumentException("Unknown distance calculator: " + name);
        }
    }

}
