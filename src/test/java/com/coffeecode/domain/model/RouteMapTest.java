package com.coffeecode.domain.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

public class RouteMapTest {

    private RouteMap routeMap;
    private GeoToolsCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new GeoToolsCalculator();
        routeMap = new RouteMap(calculator);
    }

    @Test
    void addLocation_shouldAddLocationOnly() {
        // Given
        Locations location = new Locations("A", 0.0, 0.0);

        // When
        routeMap.addLocation(location);

        // Then
        assertTrue(routeMap.getLocations().contains(location));
        assertEquals(1, routeMap.getLocations().size());
        assertEquals(0, routeMap.getRoutes().size()); // No auto-routes
    }

    @Test
    void addRoute_shouldConnectLocations() {
        // Given
        Locations start = new Locations("A", 0.0, 0.0);
        Locations end = new Locations("B", 1.0, 1.0);
        routeMap.addLocation(start);
        routeMap.addLocation(end);

        // When
        routeMap.addRoute(start.id(), end.id(), Route.RouteType.DIRECT);

        // Then
        assertEquals(1, routeMap.getRoutes().size());
        assertTrue(routeMap.hasRoute(start.id(), end.id()));
    }

    @Test
    void getRoutesFrom_shouldReturnCorrectRoutes() {
        Locations start = new Locations("A", 0, 0);
        Locations end = new Locations("B", 1, 1);

        routeMap.addLocation(start);
        routeMap.addLocation(end);
        routeMap.addBidirectionalRoute(start.id(), end.id());

        List<Route> routes = routeMap.getRoutesFrom(start.id());
        assertEquals(1, routes.size());
    }
}
