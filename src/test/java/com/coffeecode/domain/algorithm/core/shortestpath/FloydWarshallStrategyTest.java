package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;

class FloydWarshallStrategyTest extends ShortestPathBaseTest {

    private FloydWarshallStrategy floydWarshall;

    @BeforeEach
    void setUp() {
        floydWarshall = new FloydWarshallStrategy();
    }

    @Test
    void findPath_shouldFindShortestPath() {
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        List<Route> path = floydWarshall.findPath(routeMap, start.id(), end.id());
        printRoutes(path);

        assertFalse(path.isEmpty());
        assertTrue(isPathConnected(path));
        assertEquals(start.id(), path.get(0).sourceId());
        assertEquals(end.id(), path.get(path.size() - 1).targetId());
    }

    @Test
    void getAllDistances_shouldContainAllPairs() {
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        floydWarshall.findPath(routeMap, start.id(), end.id());
        Map<UUID, Map<UUID, Double>> allDistances = floydWarshall.getAllDistances();

        assertEquals(testLocations.size(), allDistances.size());
        for (Map<UUID, Double> distances : allDistances.values()) {
            assertEquals(testLocations.size(), distances.size());
        }
    }

    @Test
    void getPathCost_shouldReturnCorrectDistance() {
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        List<Route> path = floydWarshall.findPath(routeMap, start.id(), end.id());
        double expectedCost = calculateTotalDistance(path);
        double actualCost = floydWarshall.getPathCost(end.id());

        assertEquals(expectedCost, actualCost, 0.001);
    }
}
