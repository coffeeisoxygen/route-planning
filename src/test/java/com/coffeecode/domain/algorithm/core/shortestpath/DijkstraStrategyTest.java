package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.Depalgorithm.core.shortestpath.DijkstraStrategy;
import com.coffeecode.domain.location.model.Locations;
import com.coffeecode.domain.route.model.Route;

class DijkstraStrategyTest extends BaseShortestPathTest {

    private final DijkstraStrategy dijkstra = new DijkstraStrategy();

    @Test
    void testShortestPath() {
        List<Route> path = dijkstra.findPath(routeMap, jakarta.id(), surabaya.id());
        verifyShortestPath(path);
    }

    @Test
    void testCircularPath() {
        List<Route> path = dijkstra.findPath(routeMap, jakarta.id(), jakarta.id());
        verifyCircularPath(path);
    }

    @Test
    void testNoPath() {
        Locations bali = new Locations("Bali", -8.409518, 115.188919);
        routeMap.addLocation(bali);
        List<Route> path = dijkstra.findPath(routeMap, jakarta.id(), bali.id());
        assertTrue(path.isEmpty(), "Should return empty path when no route exists");
    }

}
