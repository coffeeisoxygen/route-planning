package com.coffeecode.domain.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

class RouteMapTest {
    private RouteMap routeMap;
    private Locations locationA;
    private Locations locationB;
    private Locations locationC;

    @BeforeEach
    void setUp() {
        routeMap = new RouteMap(new GeoToolsCalculator());
        locationA = new Locations("A", 0.0, 0.0);
        locationB = new Locations("B", 1.0, 1.0);
        locationC = new Locations("C", 2.0, 2.0);
    }

    @Test
    void addLocation_shouldAddLocationOnly() {
        routeMap.addLocation(locationA);
        assertTrue(routeMap.getLocation(locationA.id()).isPresent());
    }

    @Test
    void addRoute_shouldConnectLocations() {
        routeMap.addLocation(locationA);
        routeMap.addLocation(locationB);
        routeMap.addRoute(locationA.id(), locationB.id(), Route.RouteType.DIRECT);
        
        assertTrue(routeMap.hasRoute(locationA.id(), locationB.id()));
        assertEquals(1, routeMap.getRoutesFrom(locationA.id()).size());
    }

    @Test
    void addBidirectionalRoute_shouldCreateBothRoutes() {
        routeMap.addLocation(locationA);
        routeMap.addLocation(locationB);
        routeMap.addBidirectionalRoute(locationA.id(), locationB.id());
        
        assertTrue(routeMap.hasRoute(locationA.id(), locationB.id()));
        assertTrue(routeMap.hasRoute(locationB.id(), locationA.id()));
    }

    @Test
    void removeRoute_shouldDeleteRoute() {
        routeMap.addLocation(locationA);
        routeMap.addLocation(locationB);
        routeMap.addRoute(locationA.id(), locationB.id(), Route.RouteType.DIRECT);
        routeMap.removeRoute(locationA.id(), locationB.id());
        
        assertFalse(routeMap.hasRoute(locationA.id(), locationB.id()));
    }

    @Test
    void getRoutesFrom_shouldReturnCorrectRoutes() {
        routeMap.addLocation(locationA);
        routeMap.addLocation(locationB);
        routeMap.addBidirectionalRoute(locationA.id(), locationB.id());
        
        List<Route> routes = routeMap.getRoutesFrom(locationA.id());
        assertEquals(1, routes.size());
    }

    @Test
    void testPathValidation() {
        routeMap.addLocation(locationA);
        routeMap.addLocation(locationB);
        routeMap.addLocation(locationC);
        
        routeMap.addBidirectionalRoute(locationA.id(), locationB.id());
        routeMap.addBidirectionalRoute(locationB.id(), locationC.id());
        
        List<Route> routes = routeMap.getRoutesFrom(locationA.id());
        assertFalse(routes.isEmpty());
    }
}
