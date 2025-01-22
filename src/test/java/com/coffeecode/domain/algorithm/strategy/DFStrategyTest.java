package com.coffeecode.domain.algorithm.strategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Route;

class DFStrategyTest extends BasePathFindingTest {
    private DFStrategy dfs;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        dfs = new DFStrategy();
    }

    @Test
    void findPath_shouldFindPath() {
        List<Route> path = dfs.findPath(routeMap, bandung.id(), surabaya.id());
        
        assertFalse(path.isEmpty());
        assertEquals(bandung.id(), path.get(0).sourceId());
        assertEquals(surabaya.id(), path.get(path.size()-1).targetId());
    }

    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        List<Route> path = dfs.findPath(routeMap, bandung.id(), UUID.randomUUID());
        assertTrue(path.isEmpty());
    }

    @Test
    void findPath_shouldVisitAllNodes() {
        List<Route> path = dfs.findPath(routeMap, bandung.id(), surabaya.id());
        
        Set<UUID> visitedNodes = new HashSet<>();
        path.forEach(route -> {
            visitedNodes.add(route.sourceId());
            visitedNodes.add(route.targetId());
        });
        
        assertTrue(visitedNodes.contains(bandung.id()));
        assertTrue(visitedNodes.contains(jakarta.id()));
        assertTrue(visitedNodes.contains(surabaya.id()));
    }
}