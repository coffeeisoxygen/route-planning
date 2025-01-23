package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

class FloydWarshallStrategyTest {

    private RouteMap routeMap;
    private Map<String, Locations> testLocations;
    private FloydWarshallStrategy floydWarshall;

    @BeforeEach
    protected void setUp() {
        routeMap = new RouteMap(new GeoToolsCalculator());
        testLocations = new HashMap<>();
        floydWarshall = new FloydWarshallStrategy();

        // Setup 3x3 grid
        setupGrid();
        setupGridConnections();

        // Debug
        System.out.println("Floyd-Warshall Test Setup:");
        System.out.println("Locations: " + testLocations.size());
        System.out.println("Routes: " + routeMap.getRoutes().size());
    }

    private void printRoutes(List<Route> path) {
        path.forEach(route -> System.out.println(route.toString()));
    }

    private void verifyPathProperties(List<Route> path, UUID startId, UUID endId) {
        assertNotNull(path, "Path should not be null");
        assertTrue(!path.isEmpty(), "Path should not be empty");
        assertEquals(startId, path.get(0).getStart().id(), "Path should start at the correct location");
        assertEquals(endId, path.get(path.size() - 1).getEndLocation().id(), "Path should end at the correct location");
    }

    private void setupGrid() {
        // 3x3 grid
        addLocation("A", 0.0, 0.0);
        addLocation("B", 1.0, 0.0);
        addLocation("C", 2.0, 0.0);
        addLocation("D", 0.0, 1.0);
        addLocation("E", 1.0, 1.0);
        addLocation("F", 2.0, 1.0);
        addLocation("G", 0.0, 2.0);
        addLocation("H", 1.0, 2.0);
        addLocation("I", 2.0, 2.0);
    }

    private void setupGridConnections() {
        // Horizontal & Vertical connections
        addRoute("A", "B"); addRoute("B", "C");
        addRoute("D", "E"); addRoute("E", "F");
        addRoute("G", "H"); addRoute("H", "I");
        addRoute("A", "D"); addRoute("D", "G");
        addRoute("B", "E"); addRoute("E", "H");
        addRoute("C", "F"); addRoute("F", "I");
    }

    private void addLocation(String name, double lat, double lon) {
        Locations location = new Locations(name, lat, lon);
        testLocations.put(name, location);
        routeMap.addLocation(location);
    }

    private void addRoute(String from, String to) {
        routeMap.addRoute(
            testLocations.get(from).id(),
            testLocations.get(to).id(),
            Route.RouteType.DIRECT
        );
    }

    @Test
    void findPath_shouldFindShortestPath() {
        // Setup
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        // Execute
        List<Route> path = floydWarshall.findPath(routeMap, start.id(), end.id());

        // Debug
        System.out.println("Path from A to I:");
        printRoutes(path);

        // Verify
        verifyPathProperties(path, start.id(), end.id());
    }

    @Test
    void getAllDistances_shouldInitializeCorrectly() {
        // Setup
        Locations start = testLocations.get("A");
        assertNotNull(start, "Start location should exist");

        // Debug before
        System.out.println("\nBefore Floyd-Warshall:");
        System.out.println("Test locations: " + testLocations.size());
        System.out.println("Route map locations: " + routeMap.getLocations().size());

        // Execute
        floydWarshall.findPath(routeMap, start.id(), start.id());
        Map<UUID, Map<UUID, Double>> distances = floydWarshall.getAllDistances();

        // Debug after
        System.out.println("\nAfter Floyd-Warshall:");
        System.out.println("Distance matrix size: " + distances.size());
        distances.forEach((from, map)
                -> System.out.println("Distances from " + from + ": " + map.size()));

        // Verify
        verifyAllPairsDistances(distances);

        // Check specific distances
        distances.forEach((from, map) -> {
            map.forEach((to, dist) -> {
                assertTrue(dist >= 0,
                        String.format("Distance from %s to %s should be non-negative", from, to));
                if (from.equals(to)) {
                    assertEquals(0.0, dist, 0.001, "Self distance should be 0");
                }
            });
        });
    }

    @Test
    void findPath_shouldReturnEmptyForNoPath() {
        // Setup
        Locations start = testLocations.get("A");
        UUID nonExistentId = UUID.randomUUID();

        // Execute & Verify
        List<Route> path = floydWarshall.findPath(routeMap, start.id(), nonExistentId);
        assertTrue(path.isEmpty(), "Should return empty path for non-existent target");
    }

    @Test
    void getPathCost_shouldMatchActualDistance() {
        // Setup
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        // Execute
        List<Route> path = floydWarshall.findPath(routeMap, start.id(), end.id());
        double expectedCost = calculateTotalDistance(path);
        double actualCost = floydWarshall.getPathCost(end.id());

        // Verify
        assertEquals(expectedCost, actualCost, 0.001,
                String.format("Path cost mismatch: expected %.2f but got %.2f", expectedCost, actualCost));
    }

    private double calculateTotalDistance(List<Route> path) {
        return path.stream().mapToDouble(Route::getDistance).sum();
    }
}
