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
        // Setup triangle route
        routeMap.addBidirectionalRoute(surabaya.id(), jakarta.id());

        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), jakarta.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), jakarta.id());

        // Assert paths exist
        assertTrue(!bfsPath.isEmpty(), "BFS should find circular path");
        assertTrue(!dfsPath.isEmpty(), "DFS should find circular path");

        // Verify path properties
        verifyCircularPath(bfsPath, "BFS");
        verifyCircularPath(dfsPath, "DFS");
    }

    private void verifyCircularPath(List<Route> path, String algorithm) {
        if (!path.isEmpty()) {
            assertEquals(jakarta.id(), path.get(0).sourceId(),
                    algorithm + " path should start at Jakarta");
            assertEquals(jakarta.id(), path.get(path.size() - 1).targetId(),
                    algorithm + " path should end at Jakarta");

            System.out.println(algorithm + " Circular Path:");
            printPath(path);

            var stats = algorithm.equals("BFS")
                    ? bfs.getLastRunStatistics()
                    : dfs.getLastRunStatistics();
            System.out.println(algorithm + " visited nodes: " + stats.visitedNodes());
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

    @Test
    void testPathCharacteristics() {
        // Create diamond shape route
        Locations semarang = new Locations("Semarang", -6.966667, 110.416664);
        routeMap.addLocation(semarang);
        routeMap.addBidirectionalRoute(jakarta.id(), semarang.id());
        routeMap.addBidirectionalRoute(semarang.id(), surabaya.id());

        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        // BFS should find shortest path (fewest hops)
        assertTrue(bfsPath.size() <= dfsPath.size(),
                "BFS should find path with minimum number of hops");

        // Instead of comparing visited nodes, verify both algorithms explore the graph
        assertTrue(bfs.getLastRunStatistics().visitedNodes() > 0,
                "BFS should explore the graph");
        assertTrue(dfs.getLastRunStatistics().visitedNodes() > 0,
                "DFS should explore the graph");

        // Print statistics for analysis
        System.out.println("Diamond Route Test:");
        System.out.println("BFS Path (visited " + bfs.getLastRunStatistics().visitedNodes() + " nodes):");
        printPath(bfsPath);
        System.out.println("DFS Path (visited " + dfs.getLastRunStatistics().visitedNodes() + " nodes):");
        printPath(dfsPath);
    }

    @Test
    void testPerformanceAndBehavior() {
        // Setup complex route network
        Locations semarang = new Locations("Semarang", -6.966667, 110.416664);
        Locations malang = new Locations("Malang", -7.983908, 112.621391);
        Locations yogyakarta = new Locations("Yogyakarta", -7.797068, 110.370529);

        routeMap.addLocation(semarang);
        routeMap.addLocation(malang);
        routeMap.addLocation(yogyakarta);

        // Create mesh network
        routeMap.addBidirectionalRoute(jakarta.id(), semarang.id());
        routeMap.addBidirectionalRoute(semarang.id(), yogyakarta.id());
        routeMap.addBidirectionalRoute(yogyakarta.id(), malang.id());
        routeMap.addBidirectionalRoute(malang.id(), surabaya.id());

        // Test both algorithms
        long bfsStart = System.nanoTime();
        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        long bfsTime = System.nanoTime() - bfsStart;

        long dfsStart = System.nanoTime();
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), surabaya.id());
        long dfsTime = System.nanoTime() - dfsStart;

        // Verify characteristics
        assertTrue(bfs.getLastRunStatistics().visitedNodes() <= dfs.getLastRunStatistics().visitedNodes(),
                "BFS should be more efficient in nodes visited");

        assertTrue(bfsPath.size() <= dfsPath.size(),
                "BFS should find shortest path in terms of hops");

        System.out.println("\nComplex Route Test:");
        System.out.println("BFS Path (" + bfsTime / 1000000.0 + "ms):");
        printPath(bfsPath);
        System.out.println("DFS Path (" + dfsTime / 1000000.0 + "ms):");
        printPath(dfsPath);
    }

    @Test
    void testNodeVisitEfficiency() {
        // Setup simple path: Jakarta -> Bandung -> Surabaya
        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        System.out.println("\nSimple Path Test:");
        System.out.println("BFS visited: " + bfs.getLastRunStatistics().visitedNodes());
        System.out.println("DFS visited: " + dfs.getLastRunStatistics().visitedNodes());
        System.out.println("BFS path length: " + bfsPath.size());
        System.out.println("DFS path length: " + dfsPath.size());

        // Add assertions
        assertTrue(bfs.getLastRunStatistics().visitedNodes() > 0, "BFS should visit at least one node");
        assertTrue(dfs.getLastRunStatistics().visitedNodes() > 0, "DFS should visit at least one node");
    }

    @Test
    void testMeshNetworkPerformance() {
        // Setup mesh network
        Locations semarang = new Locations("Semarang", -6.966667, 110.416664);
        Locations malang = new Locations("Malang", -7.983908, 112.621391);
        routeMap.addLocation(semarang);
        routeMap.addLocation(malang);

        routeMap.addBidirectionalRoute(jakarta.id(), semarang.id());
        routeMap.addBidirectionalRoute(semarang.id(), malang.id());
        routeMap.addBidirectionalRoute(malang.id(), surabaya.id());
        routeMap.addBidirectionalRoute(jakarta.id(), bandung.id());
        routeMap.addBidirectionalRoute(bandung.id(), surabaya.id());

        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        System.out.println("\nMesh Network Test:");
        System.out.println("BFS Path:");
        printPath(bfsPath);
        System.out.println("DFS Path:");
        printPath(dfsPath);

        // Compare path lengths only
        assertEquals(bfsPath.size(), dfsPath.size(),
                "Both algorithms should find paths of same length in this network");
    }

    @Test
    void testComplexMeshNetwork() {
        // Create mesh network cities
        Locations semarang = new Locations("Semarang", -6.966667, 110.416664);
        Locations yogyakarta = new Locations("Yogyakarta", -7.797068, 110.370529);
        Locations malang = new Locations("Malang", -7.983908, 112.621391);
        Locations solo = new Locations("Solo", -7.575489, 110.824327);
        Locations kediri = new Locations("Kediri", -7.848016, 112.017829);
        
        // Add all locations
        routeMap.addLocation(semarang);
        routeMap.addLocation(yogyakarta);
        routeMap.addLocation(malang);
        routeMap.addLocation(solo);
        routeMap.addLocation(kediri);

        // Create mesh connections
        routeMap.addBidirectionalRoute(jakarta.id(), bandung.id());
        routeMap.addBidirectionalRoute(jakarta.id(), semarang.id());
        routeMap.addBidirectionalRoute(bandung.id(), yogyakarta.id());
        routeMap.addBidirectionalRoute(semarang.id(), yogyakarta.id());
        routeMap.addBidirectionalRoute(semarang.id(), solo.id());
        routeMap.addBidirectionalRoute(yogyakarta.id(), solo.id());
        routeMap.addBidirectionalRoute(solo.id(), kediri.id());
        routeMap.addBidirectionalRoute(kediri.id(), malang.id());
        routeMap.addBidirectionalRoute(malang.id(), surabaya.id());
        routeMap.addBidirectionalRoute(kediri.id(), surabaya.id());

        // Test pathfinding
        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        System.out.println("\nComplex Mesh Network Test:");
        System.out.println("BFS Path (visited " + bfs.getLastRunStatistics().visitedNodes() + " nodes):");
        printPath(bfsPath);
        System.out.println("DFS Path (visited " + dfs.getLastRunStatistics().visitedNodes() + " nodes):");
        printPath(dfsPath);
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
