package com.coffeecode.application.services.distance;

import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GeoToolsCalculator implements DistanceCalculator {

    private final CoordinateReferenceSystem crsWGS84;

    public GeoToolsCalculator() {
        try {
            this.crsWGS84 = CRS.decode("EPSG:4326", true);
        } catch (FactoryException e) {
            throw new GeoToolsException("Failed to initialize CRS", e);
        }
    }

    @Override
    public double calculate(double lat1, double lon1, double lat2, double lon2) {
        GeodeticCalculator calc = new GeodeticCalculator(crsWGS84);
        calc.setStartingGeographicPoint(lon1, lat1);
        calc.setDestinationGeographicPoint(lon2, lat2);
        double distance = calc.getOrthodromicDistance() / 1000.0;
        return roundToTwoDecimals(distance);
    }

    @Override
    public String getStrategyName() {
        return "GeoTools";
    }
}
