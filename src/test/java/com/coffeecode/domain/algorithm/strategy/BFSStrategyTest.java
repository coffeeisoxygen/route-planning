package com.coffeecode.domain.algorithm.strategy;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.algorithm.pathfinding.BFSStrategy;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;

class BFSStrategyTest extends BasePathFindingTest {

    private BFSStrategy bfs;

    @BeforeEach
    void setUp() {
        super.setUp();
        bfs = new BFSStrategy();
    }

    @Test
    void findPath_shouldFindPath() {
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        List<Route> path = bfs.findPath(routeMap, start.id(), end.id());
        printRoutes(path);

        assertFalse(path.isEmpty());
        assertEquals(start.id(), path.get(0).sourceId());
        assertEquals(end.id(), path.get(path.size() - 1).targetId());
    }

    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        List<Route> path = bfs.findPath(routeMap,
                testLocations.get("A").id(),
                UUID.randomUUID());
        assertTrue(path.isEmpty());
    }
}
