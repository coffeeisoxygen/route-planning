package com.coffeecode.domain.algorithm.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());
        
        assertFalse(path.isEmpty());
        assertEquals(jakarta.id(), path.get(0).sourceId());
        assertEquals(surabaya.id(), path.get(path.size()-1).targetId());
    }

    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        List<Route> path = dfs.findPath(routeMap, bandung.id(), UUID.randomUUID());
        assertTrue(path.isEmpty());
    }

    @Test
    void findPath_shouldVisitAllNodes() {
        routeMap.addLocation(bandung);
        routeMap.addLocation(jakarta);
        routeMap.addLocation(surabaya);
        
        // Map UUIDs to location names for debugging
        Map<UUID, String> idToName = new HashMap<>();
        idToName.put(bandung.id(), "Bandung");
        idToName.put(jakarta.id(), "Jakarta");
        idToName.put(surabaya.id(), "Surabaya");
        
        System.out.println("\nAll routes:");
        routeMap.getRoutes().forEach(r -> 
            System.out.println(String.format("%s -> %s: %.2f km", 
                idToName.get(r.sourceId()), 
                idToName.get(r.targetId()), 
                r.distance())));
        
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());
        
        System.out.println("\nFound path:");
        path.forEach(r -> 
            System.out.println(String.format("%s -> %s: %.2f km",
                idToName.get(r.sourceId()),
                idToName.get(r.targetId()),
                r.distance())));
        
        Set<UUID> visitedNodes = new HashSet<>();
        visitedNodes.add(jakarta.id()); // Add source
        path.forEach(route -> {
            visitedNodes.add(route.sourceId());
            visitedNodes.add(route.targetId());
        });
        
        assertTrue(visitedNodes.contains(jakarta.id()), "Should visit Jakarta");
        assertTrue(visitedNodes.contains(bandung.id()), "Should visit Bandung");
        assertTrue(visitedNodes.contains(surabaya.id()), "Should visit Surabaya");
    }

    @Test
    void findPath_shouldVerifyPath() {
        routeMap.addLocation(bandung);
        routeMap.addLocation(jakarta);
        routeMap.addLocation(surabaya);
        
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());
        
        assertEquals(2, path.size(), "Path should have 2 segments");
        assertEquals(jakarta.id(), path.get(0).sourceId(), "Should start from Jakarta");
        assertEquals(bandung.id(), path.get(0).targetId(), "Should go through Bandung");
        assertEquals(bandung.id(), path.get(1).sourceId(), "Should continue from Bandung");
        assertEquals(surabaya.id(), path.get(1).targetId(), "Should end at Surabaya");
    }

    @Test
    void findPath_shouldVerifyPathSegments() {
        List<Route> path = dfs.findPath(routeMap, jakarta.id(), surabaya.id());
        
        assertEquals(2, path.size(), "Path should have 2 segments");
        assertEquals(jakarta.id(), path.get(0).sourceId());
        assertEquals(bandung.id(), path.get(0).targetId());
        assertEquals(bandung.id(), path.get(1).sourceId());
        assertEquals(surabaya.id(), path.get(1).targetId());
    }

    @Test
    void findPath_shouldFindReversePath() {
        List<Route> forwardPath = dfs.findPath(routeMap, jakarta.id(), surabaya.id());
        List<Route> reversePath = dfs.findPath(routeMap, surabaya.id(), jakarta.id());
        
        assertFalse(forwardPath.isEmpty());
        assertFalse(reversePath.isEmpty());
        assertEquals(forwardPath.size(), reversePath.size());
    }
}