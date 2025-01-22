package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;

class DijkstraStrategyTest extends ShortestPathBaseTest {

    private DijkstraStrategy dijkstra;

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        dijkstra = new DijkstraStrategy();
    }

    @Test
    void findPath_shouldFindShortestPath() {
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        List<Route> path = dijkstra.findPath(routeMap, start.id(), end.id());
        printRoutes(path);

        System.out.println("Path: " + path);

        verifyPathProperties(path, start.id(), end.id());
        assertTrue(isShortestPath(path, start.id(), end.id()));
    }

    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        List<Route> path = dijkstra.findPath(routeMap,
                testLocations.get("A").id(),
                UUID.randomUUID());
        System.out.println("Path when no route: " + path);
        assertTrue(path.isEmpty());
    }

    @Test
    void getPathCost_shouldMatchPathDistance() {
        UUID startId = testLocations.get("A").id();
        UUID endId = testLocations.get("I").id();

        List<Route> path = dijkstra.findPath(routeMap, startId, endId);
        double expectedCost = calculateTotalDistance(path);
        double actualCost = dijkstra.getPathCost(endId);

        System.out.println("Expected Cost: " + expectedCost);
        System.out.println("Actual Cost: " + actualCost);

        assertEquals(expectedCost, actualCost, 0.001);
    }
}
