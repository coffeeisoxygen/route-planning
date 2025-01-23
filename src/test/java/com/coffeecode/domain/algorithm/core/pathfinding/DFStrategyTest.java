package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;

class DFStrategyTest extends PathFindingTest {

    private DFSStrategy dfs;

    @BeforeEach
    void initDFS() {
        dfs = new DFSStrategy();
    }

    @Test
    void findPath_shouldFindCorrectPath() {
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
        routeMap.addBidirectionalRoute(surabaya.id(), jakarta.id());

        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        assertFalse(path.isEmpty());
        assertTrue(dfs.getLastRunStatistics().visitedNodes() >= path.size());
    }

    @Test
    void findPath_shouldFollowDepthFirst() {
        routeMap.addBidirectionalRoute(bandung.id(), malang.id());
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        verifyPath(path, jakarta.id(), surabaya.id());
        // Verify DFS behavior by checking if it visits deeper nodes first
        assertTrue(path.stream()
                .map(r -> r.targetId())
                .anyMatch(id -> id.equals(malang.id())),
                "DFS should explore deeper paths first");
    }

    @Test
    void shouldExploreInDepthFirstOrder() {
        routeMap.addBidirectionalRoute(bandung.id(), malang.id());
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        verifyPath(path, jakarta.id(), surabaya.id());
        assertTrue(path.stream()
                .map(Route::targetId)
                .anyMatch(id -> id.equals(malang.id())),
                "DFS should explore deeper paths first");
    }

    @Test
    void shouldHandleBacktracking() {
        // Create dead-end path
        routeMap.addBidirectionalRoute(bandung.id(), malang.id());
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());

        assertFalse(path.isEmpty());
        assertTrue(dfs.getLastRunStatistics().visitedNodes() > path.size(),
                "Should show evidence of backtracking");
    }
}
