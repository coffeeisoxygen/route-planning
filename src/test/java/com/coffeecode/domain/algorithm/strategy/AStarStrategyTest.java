package com.coffeecode.domain.algorithm.strategy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.algorithm.pathfinding.DFStrategy;
import com.coffeecode.domain.algorithm.shortestpath.AStarStrategy;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;

class AStarStrategyTest extends BasePathFindingTest {

    private AStarStrategy aStar;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        aStar = new AStarStrategy();
    }

    @Test
    void findPath_shouldFindShortestPath() {
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        List<Route> path = aStar.findPath(routeMap, start.id(), end.id());
        printRoutes(path);

        assertFalse(path.isEmpty());
        assertTrue(isPathConnected(path));

        double pathDistance = calculateTotalDistance(path);
        double directDistance = routeMap.calculateDirectDistance(start.id(), end.id());

        assertTrue(pathDistance <= directDistance * 1.1); // Within 10% of direct
    }

    @Test
    void findPath_shouldOptimizeForDistance() {
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        List<Route> aStarPath = aStar.findPath(routeMap, start.id(), end.id());
        List<Route> dfsPath = new DFStrategy().findPath(routeMap, start.id(), end.id());

        double aStarDistance = calculateTotalDistance(aStarPath);
        double dfsDistance = calculateTotalDistance(dfsPath);

        assertTrue(aStarDistance <= dfsDistance);
    }
}
