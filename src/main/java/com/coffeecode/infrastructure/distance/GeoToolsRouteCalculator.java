package com.coffeecode.infrastructure.distance;

import java.util.List;

import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.stereotype.Component;
import com.coffeecode.application.port.output.RouteCalculationPort;
import com.coffeecode.domain.model.Locations;

@Component
public class GeoToolsRouteCalculator implements RouteCalculationPort {

    private final CoordinateReferenceSystem crsWGS84;

    public GeoToolsRouteCalculator() {
        try {
            this.crsWGS84 = CRS.decode("EPSG:4326", true);
        } catch (FactoryException e) {
            throw new GeoToolsException("Failed to initialize CRS", e);
        }
    }

    @Override
    public double calculateDistance(Locations from, Locations to) {
        GeodeticCalculator calc = new GeodeticCalculator(crsWGS84);
        calc.setStartingGeographicPoint(from.longitude(), from.latitude());
        calc.setDestinationGeographicPoint(to.longitude(), to.latitude());
        return calc.getOrthodromicDistance() / 1000.0; // Convert to kilometers
    }

    @Override
    public List<Locations> findShortestPath(Locations start, Locations end) {
        // For now, just return direct path
        return List.of(start, end);
    }
}
