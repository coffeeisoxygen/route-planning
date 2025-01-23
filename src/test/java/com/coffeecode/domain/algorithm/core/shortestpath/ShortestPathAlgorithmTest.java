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

class ShortestPathAlgorithmTest {

    private RouteMap routeMap;
    private DijkstraStrategy dijkstra;
    private AStarStrategy aStar;
    private FloydWarshallStrategy floydWarshall;
    private Locations jakarta, bandung, surabaya, semarang;
    private double expectedShortestDistance;

    @BeforeEach
    void setUp() {
        routeMap = new RouteMap(new GeoToolsCalculator());

        jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        bandung = new Locations("Bandung", -6.914744, 107.609810);
        surabaya = new Locations("Surabaya", -7.250445, 112.768845);
        semarang = new Locations("Semarang", -6.966667, 110.416664);

        routeMap.addLocation(jakarta);
        routeMap.addLocation(bandung);
        routeMap.addLocation(surabaya);
        routeMap.addLocation(semarang);

        routeMap.addBidirectionalRoute(jakarta.id(), bandung.id());
        routeMap.addBidirectionalRoute(bandung.id(), surabaya.id());
        routeMap.addBidirectionalRoute(jakarta.id(), semarang.id());
        routeMap.addBidirectionalRoute(semarang.id(), surabaya.id());

        dijkstra = new DijkstraStrategy();
        aStar = new AStarStrategy();
        floydWarshall = new FloydWarshallStrategy();

        // Calculate expected shortest path distance
        expectedShortestDistance = Math.min(
                routeMap.calculateDirectDistance(jakarta.id(), bandung.id())
                + routeMap.calculateDirectDistance(bandung.id(), surabaya.id()),
                routeMap.calculateDirectDistance(jakarta.id(), semarang.id())
                + routeMap.calculateDirectDistance(semarang.id(), surabaya.id())
        );
    }

    @Test
    void testDijkstraShortestPath() {
        List<Route> path = dijkstra.findPath(routeMap, jakarta.id(), surabaya.id());
        verifyShortestPath(path, "Dijkstra");
    }

    @Test
    void testAStarShortestPath() {
        List<Route> path = aStar.findPath(routeMap, jakarta.id(), surabaya.id());
        verifyShortestPath(path, "A*");
    }

    @Test
    void testFloydWarshallShortestPath() {
        List<Route> path = floydWarshall.findPath(routeMap, jakarta.id(), surabaya.id());
        verifyShortestPath(path, "Floyd-Warshall");
    }

    private void verifyShortestPath(List<Route> path, String algorithm) {
        assertNotNull(path, algorithm + " path should not be null");
        assertFalse(path.isEmpty(), algorithm + " should find a path");

        double totalDistance = calculatePathDistance(path);

        // Verify path properties
        assertEquals(jakarta.id(), path.get(0).sourceId(),
                algorithm + " path should start at source");
        assertEquals(surabaya.id(), path.get(path.size() - 1).targetId(),
                algorithm + " path should end at target");

        // Verify distance is close to expected
        assertTrue(Math.abs(totalDistance - expectedShortestDistance) < 1.0,
                algorithm + " should find shortest path");

        printPath(path, algorithm);
    }

    private double calculatePathDistance(List<Route> path) {
        return path.stream()
                .mapToDouble(Route::distance)
                .sum();
    }

    private void printPath(List<Route> path, String algorithm) {
        System.out.println("\n" + algorithm + " Path:");
        double total = 0;
        for (Route route : path) {
            System.out.printf("%s -> %s (%.2f km)\n",
                    getLocationName(route.sourceId()),
                    getLocationName(route.targetId()),
                    route.distance());
            total += route.distance();
        }
        System.out.printf("Total distance: %.2f km\n", total);
    }

    private String getLocationName(UUID id) {
        return routeMap.getLocation(id)
                .map(Locations::name)
                .orElse("Unknown");
    }
}
