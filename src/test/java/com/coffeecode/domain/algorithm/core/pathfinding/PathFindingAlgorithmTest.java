package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.algorithm.result.PathStatistics;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

public class PathFindingAlgorithmTest {

    private RouteMap routeMap;
    private BFSStrategy bfs;
    private DFSStrategy dfs;
    private Locations jakarta, bandung, surabaya;

    @BeforeEach
    void setUp() {
        // Initialize the route map with calculator
        routeMap = new RouteMap(new GeoToolsCalculator());

        // Create test locations
        jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        bandung = new Locations("Bandung", -6.914744, 107.609810);
        surabaya = new Locations("Surabaya", -7.250445, 112.768845);

        // Add locations to map
        routeMap.addLocation(jakarta);
        routeMap.addLocation(bandung);
        routeMap.addLocation(surabaya);

        // Create routes
        routeMap.addBidirectionalRoute(jakarta.id(), bandung.id());
        routeMap.addBidirectionalRoute(bandung.id(), surabaya.id());

        // Initialize algorithms
        bfs = new BFSStrategy();
        dfs = new DFSStrategy();
    }

    @Test
    void testBFSPathFinding() {
        List<Route> path = bfs.findPath(routeMap, jakarta.id(), surabaya.id());

        // Verify path exists
        assertFalse(path.isEmpty(), "Path should exist");

        // Verify path connects source to target
        assertEquals(jakarta.id(), path.get(0).sourceId(), "Path should start at Jakarta");
        assertEquals(surabaya.id(), path.get(path.size() - 1).targetId(), "Path should end at Surabaya");

        // Print path for verification
        System.out.println("BFS Path:");
        printPath(path);
    }

    @Test
    void testDFSPathFinding() {
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        assertFalse(path.isEmpty(), "Path should exist");
        assertEquals(jakarta.id(), path.get(0).sourceId());
        assertEquals(surabaya.id(), path.get(path.size() - 1).targetId());

        System.out.println("DFS Path:");
        printPath(path);
    }

    @Test
    void testNoPathExists() {
        Locations bali = new Locations("Bali", -8.409518, 115.188919);
        routeMap.addLocation(bali);
        
        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), bali.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), bali.id());
        
        assertTrue(bfsPath.isEmpty(), "BFS should return empty path");
        assertTrue(dfsPath.isEmpty(), "DFS should return empty path");
    }

    @Test
    void testCircularPath() {
        // Create circular route
        routeMap.addBidirectionalRoute(surabaya.id(), jakarta.id());
        
        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), jakarta.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), jakarta.id());
        
        // Changed assertion - path should exist
        assertTrue(!bfsPath.isEmpty(), "BFS should find circular path");
        assertTrue(!dfsPath.isEmpty(), "DFS should find circular path");
        
        // Verify start/end points match
        if (!bfsPath.isEmpty()) {
            assertEquals(jakarta.id(), bfsPath.get(0).sourceId());
            assertEquals(jakarta.id(), bfsPath.get(bfsPath.size()-1).targetId());
            System.out.println("BFS Circular Path:");
            printPath(bfsPath);
        }

        if (!dfsPath.isEmpty()) {
            assertEquals(jakarta.id(), dfsPath.get(0).sourceId());
            assertEquals(jakarta.id(), dfsPath.get(dfsPath.size()-1).targetId());
            System.out.println("DFS Circular Path:");
            printPath(dfsPath);
        }
    }

    @Test
    void testMultiplePaths() {
        Locations semarang = new Locations("Semarang", -6.966667, 110.416664);
        routeMap.addLocation(semarang);
        routeMap.addBidirectionalRoute(jakarta.id(), semarang.id());
        routeMap.addBidirectionalRoute(semarang.id(), surabaya.id());
        
        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), surabaya.id());
        
        assertFalse(bfsPath.isEmpty());
        assertFalse(dfsPath.isEmpty());
    }

    @Test
    void testInvalidInputs() {
        UUID invalidId = UUID.randomUUID();
        List<Route> bfsPath = bfs.findPath(routeMap, invalidId, jakarta.id());
        List<Route> dfsPath = dfs.findPath(routeMap, invalidId, jakarta.id());
        
        assertTrue(bfsPath.isEmpty());
        assertTrue(dfsPath.isEmpty());
    }

    @Test
    void testPerformanceComparison() {
        bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        PathStatistics bfsStats = bfs.getLastRunStatistics();
        
        dfs.findPath(routeMap, jakarta.id(), surabaya.id());
        PathStatistics dfsStats = dfs.getLastRunStatistics();
        
        assertTrue(bfsStats.visitedNodes() > 0, "BFS should visit at least one node");
        assertTrue(dfsStats.visitedNodes() > 0, "DFS should visit at least one node");
        
        System.out.println("BFS visited nodes: " + bfsStats.visitedNodes());
        System.out.println("DFS visited nodes: " + dfsStats.visitedNodes());
    }

    private void printPath(List<Route> path) {
        double totalDistance = 0;
        for (Route route : path) {
            System.out.printf("%s -> %s (%.2f km)\n",
                    getLocationName(route.sourceId()),
                    getLocationName(route.targetId()),
                    route.distance());
            totalDistance += route.distance();
        }
        System.out.printf("Total distance: %.2f km\n", totalDistance);
    }

    private String getLocationName(UUID id) {
        return routeMap.getLocation(id)
                .map(Locations::name)
                .orElse("Unknown");
    }
}
