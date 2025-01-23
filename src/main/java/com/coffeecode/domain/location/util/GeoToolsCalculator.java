package com.coffeecode.domain.location.util;

import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.coffeecode.domain.exception.InvalidLocationException;
import com.coffeecode.domain.location.exception.DistanceCalculatorException;
import com.coffeecode.domain.location.validation.LocationValidator;

@Component
@Primary
public class GeoToolsCalculator implements DistanceCalculator {

    private final CoordinateReferenceSystem crsWGS84;

    public GeoToolsCalculator() {
        try {
            this.crsWGS84 = CRS.decode("EPSG:4326", true);
        } catch (FactoryException e) {
            throw new DistanceCalculatorException("Failed to initialize CRS", e);
        }
    }

    @Override
    public double calculateDistance(double fromLat, double fromLon, double toLat, double toLon) {
        try {
            LocationValidator.validateCoordinates(fromLat, fromLon);
            LocationValidator.validateCoordinates(toLat, toLon);
        } catch (InvalidLocationException e) {
            throw new DistanceCalculatorException("Invalid coordinates provided", e);
        }

        try {
            GeodeticCalculator calc = new GeodeticCalculator(crsWGS84);
            calc.setStartingGeographicPoint(fromLon, fromLat);
            calc.setDestinationGeographicPoint(toLon, toLat);
            return calc.getOrthodromicDistance() / 1000.0;
        } catch (IllegalArgumentException e) {
            throw new DistanceCalculatorException("Invalid coordinates provided", e);
        }
    }
}
