package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;

class FloydWarshallStrategyTest extends ShortestPathBaseTest {

    private FloydWarshallStrategy floydWarshall;

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        floydWarshall = new FloydWarshallStrategy();
    }

    @Test
    void findPath_shouldFindShortestPath() {
        Locations start = testLocations.get("A");
        assertNotNull(start, "Start location should exist");

        Locations end = testLocations.get("I");
        assertNotNull(end, "End location should exist");

        List<Route> path = floydWarshall.findPath(routeMap, start.id(), end.id());
        printRoutes(path);

        assertFalse(path.isEmpty(), "Path should not be empty");
        assertTrue(isPathConnected(path), "Path should be connected");
        assertEquals(start.id(), path.get(0).sourceId(), "Path should start at source");
        assertEquals(end.id(), path.get(path.size() - 1).targetId(), "Path should end at target");
    }

    @Test
    void getAllDistances_shouldContainAllPairs() {
        // Setup
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");
        assertNotNull(start, "Start location should exist");
        assertNotNull(end, "End location should exist");

        // Debug info before
        System.out.println("Test Locations size: " + testLocations.size());
        System.out.println("Route Map Locations size: " + routeMap.getLocations().size());
        testLocations.forEach((k, v) -> System.out.println(k + ": " + v.id()));

        // Execute
        floydWarshall.findPath(routeMap, start.id(), end.id());
        Map<UUID, Map<UUID, Double>> allDistances = floydWarshall.getAllDistances();

        // Debug info after
        System.out.println("All Distances size: " + allDistances.size());
        System.out.println("First entry size: " + allDistances.values().iterator().next().size());

        // Verify
        int expectedSize = testLocations.size();
        assertEquals(expectedSize, allDistances.size(),
                String.format("Expected %d distances maps but got %d", expectedSize, allDistances.size()));

        for (Map<UUID, Double> distances : allDistances.values()) {
            assertEquals(expectedSize, distances.size(),
                    String.format("Expected %d distances but got %d", expectedSize, distances.size()));
        }
    }

    @Test
    void getPathCost_shouldReturnCorrectDistance() {
        // Setup
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");
        assertNotNull(start, "Start location should exist");
        assertNotNull(end, "End location should exist");

        // Execute
        List<Route> path = floydWarshall.findPath(routeMap, start.id(), end.id());
        assertFalse(path.isEmpty(), "Path should exist");

        // Verify distances
        Map<UUID, Map<UUID, Double>> allDistances = floydWarshall.getAllDistances();
        assertNotNull(allDistances.get(start.id()), "Start node distances should exist");

        double expectedCost = calculateTotalDistance(path);
        double actualCost = allDistances.get(start.id()).get(end.id());

        // Debug output
        System.out.println(String.format("Path from %s to %s", start.name(), end.name()));
        System.out.println(String.format("Expected cost: %.2f", expectedCost));
        System.out.println(String.format("Actual cost: %.2f", actualCost));
        printRoutes(path);

        assertEquals(expectedCost, actualCost, 0.001);
    }
}
