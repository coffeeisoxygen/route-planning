package com.coffeecode.domain.algorithm.strategy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Route;

class AStarStrategyTest extends BasePathFindingTest {

    private AStarStrategy aStar = new AStarStrategy();

    @Test
    void findPath_shouldFindShortestPath() {
        List<Route> path = aStar.findPath(routeMap, bandung.id(), surabaya.id());

        assertFalse(path.isEmpty());
        assertEquals(bandung.id(), path.get(0).sourceId());
        assertEquals(surabaya.id(), path.get(path.size() - 1).targetId());

        // Verify it's shortest by comparing with direct route
        double pathDistance = path.stream()
                .mapToDouble(Route::distance)
                .sum();
        double directDistance = routeMap.calculateDirectDistance(
                bandung.id(), surabaya.id());

        assertTrue(pathDistance <= directDistance * 1.1); // Within 10% of direct
    }

    @Test
    void findPath_shouldOptimizeForDistance() {
        List<Route> aStarPath = aStar.findPath(routeMap, bandung.id(), surabaya.id());
        List<Route> dfsPath = new DFStrategy().findPath(routeMap, bandung.id(), surabaya.id());

        double aStarDistance = aStarPath.stream()
                .mapToDouble(Route::distance)
                .sum();
        double dfsDistance = dfsPath.stream()
                .mapToDouble(Route::distance)
                .sum();

        assertTrue(aStarDistance <= dfsDistance);
    }
}
