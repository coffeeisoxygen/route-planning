package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

class PathFindingTest {

    private static final Logger logger = LoggerFactory.getLogger(PathFindingTest.class);

    private RouteMap routeMap;
    private BFSStrategy bfs;
    private DFStrategy dfs;
    private Locations jakarta, bandung, surabaya, semarang, yogyakarta, malang;

    @BeforeEach
    void setUp() {
        routeMap = new RouteMap(new GeoToolsCalculator());
        bfs = new BFSStrategy();
        dfs = new DFStrategy();

        // Create test locations
        jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        bandung = new Locations("Bandung", -6.914744, 107.609810);
        surabaya = new Locations("Surabaya", -7.250445, 112.768845);
        semarang = new Locations("Semarang", -6.966667, 110.416664);
        yogyakarta = new Locations("Yogyakarta", -7.797068, 110.370529);
        malang = new Locations("Malang", -7.983908, 112.621391);

        // Add locations
        routeMap.addLocation(jakarta);
        routeMap.addLocation(bandung);
        routeMap.addLocation(surabaya);
        routeMap.addLocation(semarang);
        routeMap.addLocation(yogyakarta);
        routeMap.addLocation(malang);

        // Create complex route network
        routeMap.addBidirectionalRoute(jakarta.id(), bandung.id());
        routeMap.addBidirectionalRoute(jakarta.id(), semarang.id());
        routeMap.addBidirectionalRoute(bandung.id(), yogyakarta.id());
        routeMap.addBidirectionalRoute(semarang.id(), surabaya.id());
        routeMap.addBidirectionalRoute(yogyakarta.id(), surabaya.id());
        routeMap.addBidirectionalRoute(surabaya.id(), malang.id());
    }

    @Test
    void testMultiplePathsBFS() {
        List<Route> path = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        assertFalse(path.isEmpty(), "Should find a path");
        logger.info("BFS Path (Multiple Options):");
        printPath(path);
    }

    @Test
    void testMultiplePathsDFS() {
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());
        assertFalse(path.isEmpty(), "Should find a path");
        logger.info("DFS Path (Multiple Options):");
        printPath(path);
    }

    @Test
    void testNoPathScenario() {
        // Create isolated location
        Locations bali = new Locations("Bali", -8.409518, 115.188919);
        routeMap.addLocation(bali);

        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), bali.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), bali.id());

        assertTrue(bfsPath.isEmpty(), "BFS should return empty path");
        assertTrue(dfsPath.isEmpty(), "DFS should return empty path");
        logger.info("No path found test passed");
    }

    @Test
    void testInvalidInput() {
        UUID invalidId = UUID.randomUUID();
        List<Route> path = bfs.findPath(routeMap, invalidId, jakarta.id());
        assertTrue(path.isEmpty(), "Should handle invalid input gracefully");
    }

    @Test
    void testCircularPath() {
        // Create circular path
        routeMap.addBidirectionalRoute(semarang.id(), jakarta.id());

        List<Route> path = bfs.findPath(routeMap, jakarta.id(), jakarta.id());

        assertFalse(path.isEmpty(), "Should find a circular path");
        printPath(path);

        // Verify path forms a cycle
        if (!path.isEmpty()) {
            assertEquals(jakarta.id(), path.get(0).sourceId());
            assertEquals(jakarta.id(), path.get(path.size() - 1).targetId());
        }
    }

    @Test
    void testIntermediateCities() {
        // Remove direct route if exists
        routeMap.removeRoute(jakarta.id(), surabaya.id());
        routeMap.removeRoute(surabaya.id(), jakarta.id());

        // Force path through Yogyakarta
        routeMap.removeRoute(semarang.id(), surabaya.id());

        List<Route> pathThroughYogyakarta = bfs.findPath(routeMap, jakarta.id(), surabaya.id());

        boolean containsYogyakarta = pathThroughYogyakarta.stream()
                .anyMatch(r -> getLocationName(r.sourceId()).equals("Yogyakarta")
                || getLocationName(r.targetId()).equals("Yogyakarta"));

        logger.info("Path through intermediate cities:");
        printPath(pathThroughYogyakarta);
        assertTrue(containsYogyakarta, "Path should include Yogyakarta");
        assertFalse(pathThroughYogyakarta.isEmpty(), "Should find a path");
    }

    @Test
    void testAlternativePaths() {
        // Add alternative route with longer distance
        routeMap.addBidirectionalRoute(jakarta.id(), surabaya.id());
        List<Route> bfsPath = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        List<Route> dfsPath = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        logger.info("BFS Alternative Path:");
        printPath(bfsPath);
        logger.info("DFS Alternative Path:");
        printPath(dfsPath);

        assertNotNull(bfsPath);
        assertNotNull(dfsPath);
    }

    @Test
    void testPerformance() {
        long startTime = System.nanoTime();
        bfs.findPath(routeMap, jakarta.id(), malang.id());
        long bfsTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        dfs.findPath(routeMap, jakarta.id(), malang.id());
        long dfsTime = System.nanoTime() - startTime;

        logger.info("Performance Test:");
        logger.info("BFS time: {} ms", bfsTime / 1_000_000.0);
        logger.info("DFS time: {} ms", dfsTime / 1_000_000.0);
    }

    private void printPath(List<Route> path) {
        if (path.isEmpty()) {
            logger.info("No path found!");
            return;
        }

        double totalDistance = 0;
        for (Route route : path) {
            logger.info("  {} -> {} ({}km)",
                    getLocationName(route.sourceId()),
                    getLocationName(route.targetId()),
                    String.format("%.2f", route.distance()));
            totalDistance += route.distance();
        }
        logger.info("Total distance: {}km", String.format("%.2f", totalDistance));
    }

    private String getLocationName(UUID id) {
        return routeMap.getLocation(id)
                .map(Locations::name)
                .orElse("Unknown");
    }
}
