package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

class AStarStrategyTest extends ShortestPathBaseTest {

    private AStarStrategy aStar;

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        aStar = new AStarStrategy();
    }

    @Test
    void findPath_shouldFindPath() {
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        System.out.println("Finding path from " + start.id() + " to " + end.id());
        List<Route> path = aStar.findPath(routeMap, start.id(), end.id());
        printRoutes(path);

        assertFalse(path.isEmpty());
        assertTrue(isPathConnected(path));
        assertEquals(start.id(), path.get(0).sourceId());
        assertEquals(end.id(), path.get(path.size() - 1).targetId());
    }

    @Test
    void findPath_shouldBeOptimal() {
        UUID startId = testLocations.get("A").id();
        UUID endId = testLocations.get("I").id();

        System.out.println("Comparing A* and Dijkstra paths from " + startId + " to " + endId);
        List<Route> aStarPath = aStar.findPath(routeMap, startId, endId);
        List<Route> dijkstraPath = new DijkstraStrategy().findPath(routeMap, startId, endId);

        double aStarCost = calculateTotalDistance(aStarPath);
        double dijkstraCost = calculateTotalDistance(dijkstraPath);

        System.out.println("A* cost: " + aStarCost + ", Dijkstra cost: " + dijkstraCost);
        assertTrue(aStarCost <= dijkstraCost * 1.001); // Within 0.1% of optimal
    }

    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        // Create new empty route map for this test
        RouteMap isolatedMap = new RouteMap(new GeoToolsCalculator());

        // Add two disconnected locations
        Locations start = new Locations("START", 0.0, 0.0);
        Locations end = new Locations("END", 1.0, 1.0);

        isolatedMap.addLocation(start);
        isolatedMap.addLocation(end);
        // No routes added = disconnected

        List<Route> path = aStar.findPath(isolatedMap, start.id(), end.id());
        assertTrue(path.isEmpty(), "Path should be empty when no routes exist between locations");
    }


    @Test
    void getPathCost_shouldMatchPathDistance() {
        // Setup
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");
        assertNotNull(start, "Start location not found");
        assertNotNull(end, "End location not found");

        // Find path
        List<Route> path = aStar.findPath(routeMap, start.id(), end.id());
        assertFalse(path.isEmpty(), "Path should exist");

        // Debug output
        System.out.println("Path from " + start.name() + " to " + end.name() + ":");
        printRoutes(path);

        // Calculate costs
        double expectedCost = calculateTotalDistance(path);
        System.out.println("Expected cost: " + expectedCost);

        double actualCost = aStar.getPathCost(end.id());
        System.out.println("Actual cost: " + actualCost);

        // Verify
        assertTrue(expectedCost > 0, "Expected cost should be positive");
        assertEquals(expectedCost, actualCost, 0.001,
                String.format("Path cost mismatch: expected %.2f but got %.2f",
                        expectedCost, actualCost));
    }
}
