package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;

class BFSStrategyTest extends PathFindingTest {

    @Test
    void findPath_shouldFindCorrectPath() {
        BFSStrategy bfs = new BFSStrategy();
        List<Route> path = bfs.findPath(routeMap, jakarta.id(), surabaya.id());

        verifyPathResult(path, bfs);
        assertEquals("Breadth First Search", bfs.getAlgorithmName());
        assertEquals(2, path.size(), "Path should have 2 segments");
    }

    @Test
    void findPath_noPath_shouldReturnEmpty() {
        BFSStrategy bfs = new BFSStrategy();
        Locations bali = new Locations("Bali", -8.409518, 115.188919);
        routeMap.addLocation(bali);

        List<Route> path = bfs.findPath(routeMap, jakarta.id(), bali.id());

        assertTrue(path.isEmpty());
        assertNotNull(bfs.getLastRunStatistics());
    }

    @Test
    void findPath_shouldFindShortestPath() {
        BFSStrategy bfs = new BFSStrategy();
        List<Route> path = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        verifyPath(path, jakarta.id(), surabaya.id());
        
        // Add direct route
        routeMap.addBidirectionalRoute(jakarta.id(), surabaya.id());
        List<Route> directPath = bfs.findPath(routeMap, jakarta.id(), surabaya.id());
        
        assertTrue(calculatePathDistance(directPath) <= calculatePathDistance(path),
            "BFS should find shortest path available");
    }
}
