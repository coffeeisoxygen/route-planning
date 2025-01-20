package com.coffeecode.util.distance;

public class DistanceCalculatorFactory {

    private DistanceCalculatorFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static DistanceCalculator getDistanceCalculator(DistanceCalulatorName name) {
        switch (name) {
            case HAVERSINE -> {
                return new HaversineCalculator();
            }
            case VINCENTY -> {
                return new VincentyCalculator();
            }
            case EQUIRECTANGULAR -> {
                return new EquirectangularCalculator();
            }
            default ->
                throw new IllegalArgumentException("Unknown distance calculator: " + name);
        }
    }

}
