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
import com.coffeecode.domain.model.Route.RouteType;

class FloydWarshallStrategyTest extends ShortestPathBaseTest {

    private FloydWarshallStrategy floydWarshall;

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        floydWarshall = new FloydWarshallStrategy();
        // Populate routeMap with locations and routes
        routeMap.addLocation(testLocations.get("A"));
        routeMap.addLocation(testLocations.get("B"));
        routeMap.addLocation(testLocations.get("C"));
        routeMap.addLocation(testLocations.get("D"));
        routeMap.addLocation(testLocations.get("E"));
        routeMap.addLocation(testLocations.get("F"));
        routeMap.addLocation(testLocations.get("G"));
        routeMap.addLocation(testLocations.get("H"));
        routeMap.addLocation(testLocations.get("I"));

        routeMap.addWeightedRoute(testLocations.get("A").id(), testLocations.get("B").id(), 1.0, RouteType.DIRECT);
        routeMap.addWeightedRoute(testLocations.get("B").id(), testLocations.get("C").id(), 1.0, RouteType.DIRECT);
        routeMap.addWeightedRoute(testLocations.get("C").id(), testLocations.get("D").id(), 1.0, RouteType.DIRECT);
        routeMap.addWeightedRoute(testLocations.get("D").id(), testLocations.get("E").id(), 1.0, RouteType.DIRECT);
        routeMap.addWeightedRoute(testLocations.get("E").id(), testLocations.get("F").id(), 1.0, RouteType.DIRECT);
        routeMap.addWeightedRoute(testLocations.get("F").id(), testLocations.get("G").id(), 1.0, RouteType.DIRECT);
        routeMap.addWeightedRoute(testLocations.get("G").id(), testLocations.get("H").id(), 1.0, RouteType.DIRECT);
        routeMap.addWeightedRoute(testLocations.get("H").id(), testLocations.get("I").id(), 1.0, RouteType.DIRECT);
    }

    @Test
    void setUp_shouldInitializeCorrectly() {
        assertEquals(9, testLocations.size(), "Should have 9 test locations");
        assertEquals(9, routeMap.getLocations().size(), "RouteMap should have 9 locations");
        assertEquals(12, routeMap.getRoutes().size(), "Should have 12 bidirectional routes");
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
        // Setup - use smaller test case
        Locations a = testLocations.get("A");
        Locations b = testLocations.get("B");
        Locations c = testLocations.get("C");

        // Execute
        floydWarshall.findPath(routeMap, a.id(), c.id());
        Map<UUID, Map<UUID, Double>> allDistances = floydWarshall.getAllDistances();

        // Verify matrix properties
        assertEquals(9, allDistances.size(), "Should have distances for all vertices");
        allDistances.values().forEach(distances -> 
            assertEquals(9, distances.size(), "Each vertex should have distances to all others")
        );

        // Verify specific distances
        double abDist = allDistances.get(a.id()).get(b.id());
        double bcDist = allDistances.get(b.id()).get(c.id());
        double acDist = allDistances.get(a.id()).get(c.id());

        assertTrue(abDist > 0, "A->B distance should be positive");
        assertTrue(bcDist > 0, "B->C distance should be positive");
        assertTrue(acDist >= abDist + bcDist, "A->C should not be shorter than A->B->C");
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
