package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.Route.RouteType;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

class PathFindingTest {

    private BFSStrategy bfsStrategy;
    private DFStrategy dfsStrategy;
    private RouteMap map;
    private UUID nodeA, nodeB, nodeC, nodeD;

    @BeforeEach
    void setUp() {
        bfsStrategy = new BFSStrategy();
        dfsStrategy = new DFStrategy();
        map = new RouteMap(new GeoToolsCalculator());

        // Create test nodes
        nodeA = UUID.randomUUID();
        nodeB = UUID.randomUUID();
        nodeC = UUID.randomUUID();
        nodeD = UUID.randomUUID();

        // Create test routes
        map.addRoute(nodeA, nodeB, RouteType.DIRECT);
        map.addRoute(nodeB, nodeC, RouteType.DIRECT);
        map.addRoute(nodeA, nodeD, RouteType.DIRECT);
        map.addRoute(nodeD, nodeC, RouteType.DIRECT);
    }

    @Test
    void testBFSPathFinding() {
        List<Route> path = bfsStrategy.findPath(map, nodeA, nodeC);

        // Verify path exists
        assertFalse(path.isEmpty());

        // Print path for verification
        System.out.println("\nBFS Path from A to C:");
        printPath(path);

        // Verify path correctness
        assertEquals(nodeA, path.get(0).sourceId());
        assertEquals(nodeC, path.get(path.size() - 1).targetId());
    }

    @Test
    void testDFSPathFinding() {
        List<Route> path = dfsStrategy.findPath(map, nodeA, nodeC);

        // Verify path exists
        assertFalse(path.isEmpty());

        // Print path for verification
        System.out.println("\nDFS Path from A to C:");
        printPath(path);

        // Verify path correctness
        assertEquals(nodeA, path.get(0).sourceId());
        assertEquals(nodeC, path.get(path.size() - 1).targetId());
    }

    @Test
    void testNoPathExists() {
        UUID unreachableNode = UUID.randomUUID();

        List<Route> bfsPath = bfsStrategy.findPath(map, nodeA, unreachableNode);
        List<Route> dfsPath = dfsStrategy.findPath(map, nodeA, unreachableNode);

        assertTrue(bfsPath.isEmpty());
        assertTrue(dfsPath.isEmpty());

        System.out.println("\nNo path exists to unreachable node - verified");
    }

    // Helper method to print path details
    private void printPath(List<Route> path) {
        System.out.println("Path length: " + path.size());
        System.out.println("Route details:");

        double totalDistance = 0;
        for (Route route : path) {
            System.out.printf("  %s -> %s (distance: %.2f)\n",
                    shortenUUID(route.sourceId()),
                    shortenUUID(route.targetId()),
                    route.distance());
            totalDistance += route.distance();
        }
        System.out.printf("Total distance: %.2f\n", totalDistance);
    }

    // Helper method to shorten UUID for readable output
    private String shortenUUID(UUID id) {
        return id.toString().substring(0, 4);
    }
}
