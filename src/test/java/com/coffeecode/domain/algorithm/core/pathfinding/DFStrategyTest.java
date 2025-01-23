package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;

class DFStrategyTest extends PathFindingTest {

    @Test
    void findPath_shouldFindCorrectPath() {
        DFSStrategy dfs = new DFSStrategy();
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        verifyPathResult(path, dfs);
        assertEquals("Depth First Search", dfs.getAlgorithmName());
        assertEquals(2, path.size(), "Path should have 2 segments");

        // Verify path order
        assertEquals(jakarta.id(), path.get(0).sourceId());
        assertEquals(surabaya.id(), path.get(path.size() - 1).targetId());
    }

    @Test
    void findPath_noPath_shouldReturnEmpty() {
        DFSStrategy dfs = new DFSStrategy();
        Locations bali = new Locations("Bali", -8.409518, 115.188919);
        routeMap.addLocation(bali);

        List<Route> path = dfs.findPath(routeMap, jakarta.id(), bali.id());

        assertTrue(path.isEmpty());
        var stats = dfs.getLastRunStatistics();
        assertNotNull(stats);
        assertTrue(stats.visitedNodes() > 0);
    }

    @Test
    void findPath_circularPath_shouldFindPath() {
        DFSStrategy dfs = new DFSStrategy();
        routeMap.addBidirectionalRoute(surabaya.id(), jakarta.id());

        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        assertFalse(path.isEmpty());
        assertTrue(dfs.getLastRunStatistics().visitedNodes() >= path.size());
    }
}
