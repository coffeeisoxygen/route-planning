package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coffeecode.domain.algorithm.api.PathFinding;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

abstract class PathFindingTest {

    private static final Logger logger = LoggerFactory.getLogger(PathFindingTest.class);

    protected RouteMap routeMap;
    protected Locations jakarta, bandung, surabaya, semarang, yogyakarta, malang;
    protected PathFinding bfs, dfs;

    @BeforeEach
    void setUp() {
        routeMap = new RouteMap(new GeoToolsCalculator());

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
        bfs = new BFSStrategy();
        dfs = new DFSStrategy();
    }

    protected void verifyPathResult(List<Route> path, PathFinding algorithm) {
        assertNotNull(path, "Path should not be null");
        assertFalse(path.isEmpty(), "Path should not be empty");

        var stats = algorithm.getLastRunStatistics();
        assertNotNull(stats, "Statistics should be tracked");
        assertTrue(stats.visitedNodes() > 0, "Should visit nodes");
        assertTrue(stats.getExecutionTime() >= 0, "Should track execution time");
    }

    // Add helper method for path validation
    protected void verifyPath(List<Route> path, UUID source, UUID target) {
        assertNotNull(path);
        assertFalse(path.isEmpty());
        assertEquals(source, path.get(0).sourceId());
        assertEquals(target, path.get(path.size() - 1).targetId());
    }

    // Add helper for distance calculation
    protected double calculatePathDistance(List<Route> path) {
        return path.stream()
                .mapToDouble(Route::distance)
                .sum();
    }

    protected void printPath(List<Route> path) {
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
