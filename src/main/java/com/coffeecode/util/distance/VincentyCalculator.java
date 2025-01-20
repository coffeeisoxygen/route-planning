package com.coffeecode.util.distance;

public class VincentyCalculator implements DistanceCalculator {

    private static final double A = 6378137.0; // WGS-84 semi-major axis
    private static final double B = 6356752.314245; // WGS-84 semi-minor axis
    private static final double F = 1 / 298.257223563; // WGS-84 flattening

    @Override
    public double calculate(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double L = lon2 - lon1;
        double U1 = Math.atan((1 - F) * Math.tan(lat1));
        double U2 = Math.atan((1 - F) * Math.tan(lat2));
        double sinU1 = Math.sin(U1);
        double cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2);
        double cosU2 = Math.cos(U2);

        // Simplified version for basic calculation
        double lambda = L;
        double sinLambda = Math.sin(lambda);
        double cosLambda = Math.cos(lambda);
        double sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
                + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
        double cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
        double sigma = Math.atan2(sinSigma, cosSigma);

        return A * sigma;
    }

    @Override
    public String getStrategyName() {
        return "Vincenty";
    }

}
