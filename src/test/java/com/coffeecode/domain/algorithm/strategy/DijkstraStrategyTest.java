package com.coffeecode.domain.algorithm.strategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

class DijkstraStrategyTest extends BasePathFindingTest {

    private DijkstraStrategy dijkstra;
    private Map<UUID, String> idToName;
    private Locations loc1;
    private Locations loc3;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        dijkstra = new DijkstraStrategy();
        loc1 = new Locations(UUID.randomUUID(), "Location 1", 0.0, 0.0);
        loc3 = new Locations(UUID.randomUUID(), "Location 3", 1.0, 1.0);
        loc3 = new Locations(UUID.randomUUID(), "Location 3", 1.0, 1.0);
        idToName.put(loc1.id(), loc1.name());
        idToName.put(loc3.id(), loc3.name());
        idToName.put(bandung.id(), "Bandung");
        idToName.put(jakarta.id(), "Jakarta");
        idToName.put(surabaya.id(), "Surabaya");
        idToName.put(jakarta.id(), "Jakarta");
        idToName.put(surabaya.id(), "Surabaya");
    }

    @Test
    void findPath_shouldReturnShortestPath() {
        List<Route> path = dijkstra.findPath(routeMap, loc1.id(), loc3.id());

        assertFalse(path.isEmpty());
        assertEquals(loc1.id(), path.get(0).sourceId());
        assertEquals(loc3.id(), path.get(path.size() - 1).targetId());

        // Verify it's shortest path by checking total distance
        double totalDistance = path.stream()
                .mapToDouble(Route::distance)
                .sum();

        Optional<Route> directRoute = routeMap.getRoute(loc1.id(), loc3.id());
        assertTrue(directRoute.isPresent());
        assertEquals(directRoute.get().distance(), totalDistance, 0.001);
    }

    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        List<Route> path = dijkstra.findPath(routeMap, loc1.id(), UUID.randomUUID());
        assertTrue(path.isEmpty());
    }

    @Test
    void findPath_shouldFindShortestPath() {
        List<Route> path = dijkstra.findPath(routeMap, jakarta.id(), surabaya.id());

        System.out.println("\nAll routes:");
        printRoutes(routeMap.getRoutes());

        System.out.println("\nFound path:");
        printRoutes(path);

        double totalDistance = calculateTotalDistance(path);
        System.out.println(String.format("\nTotal distance: %.2f km", totalDistance));

        assertFalse(path.isEmpty());
        assertTrue(isPathConnected(path));
        assertTrue(isShortestPath(path, routeMap, jakarta.id(), surabaya.id()));
    }

    private void printRoutes(Collection<Route> routes) {
        routes.forEach(r -> System.out.println(String.format("%s -> %s: %.2f km",
                idToName.get(r.sourceId()),
                idToName.get(r.targetId()),
                r.distance())));
    }

    private boolean isPathConnected(List<Route> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            if (!path.get(i).targetId().equals(path.get(i + 1).sourceId())) {
                return false;
            }
        }
        return true;
    }

    private double calculateTotalDistance(List<Route> path) {
        return path.stream().mapToDouble(Route::distance).sum();
    }

    private boolean isShortestPath(List<Route> path, RouteMap routeMap, UUID sourceId, UUID targetId) {
        double pathDistance = calculateTotalDistance(path);
        List<Route> directPath = dijkstra.findPath(routeMap, sourceId, targetId);
        double directPathDistance = calculateTotalDistance(directPath);
        return pathDistance <= directPathDistance;
    }
}
