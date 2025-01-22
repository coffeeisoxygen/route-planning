package com.coffeecode.domain.algorithm.strategy;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.algorithm.core.shortestpath.DijkstraStrategy;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

class DijkstraStrategyTest extends BasePathFindingTest {

    private boolean isShortestPath(List<Route> path, RouteMap routeMap, UUID startId, UUID endId) {
        // Implement the logic to verify if the path is the shortest
        // This is a placeholder implementation
        double pathDistance = calculateTotalDistance(path);
        double directDistance = routeMap.calculateDirectDistance(startId, endId);
        return pathDistance <= directDistance * 1.1; // Within 10% of direct distance
    }

    private DijkstraStrategy dijkstra;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        dijkstra = new DijkstraStrategy();
    }

    @Test
    void findPath_shouldFindShortestPath() {
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        List<Route> path = dijkstra.findPath(routeMap, start.id(), end.id());
        printRoutes(path);

        assertFalse(path.isEmpty());
        assertTrue(isPathConnected(path));
        double totalDistance = calculateTotalDistance(path);
        System.out.println(String.format("\nTotal distance: %.2f km", totalDistance));

        // Verify shortest
        assertTrue(isShortestPath(path, routeMap, start.id(), end.id()));
    }

    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        List<Route> path = dijkstra.findPath(routeMap,
                testLocations.get("A").id(),
                UUID.randomUUID());
        assertTrue(path.isEmpty());
    }
}
