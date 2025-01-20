package com.coffeecode.util.distance;

public class EquirectangularCalculator implements DistanceCalculator {

    private static final double R = 6371.0; // Earth radius in kilometers

    @Override
    public double calculate(double lat1, double lon1, double lat2, double lon2) {
        double x = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2);
        double y = lat2 - lat1;
        return Math.sqrt(x * x + y * y) * R;
    }

    @Override
    public String getStrategyName() {
        return "Equirectangular";
    }

}
