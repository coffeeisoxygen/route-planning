package com.coffeecode.util.distance;

import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GeoToolsCalculator implements DistanceCalculator {
    private static final CoordinateReferenceSystem CRS_WGS84;
    
    static {
        try {
            CRS_WGS84 = CRS.decode("EPSG:4326");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize CRS", e);
        }
    }

    @Override
    public double calculate(double lat1, double lon1, double lat2, double lon2) {
        GeodeticCalculator calc = new GeodeticCalculator(CRS_WGS84);
        calc.setStartingGeographicPoint(lon1, lat1);
        calc.setDestinationGeographicPoint(lon2, lat2);
        return calc.getOrthodromicDistance() / 1000.0; // Convert to km
    }

    @Override
    public String getStrategyName() {
        return "GeoTools";
    }
}