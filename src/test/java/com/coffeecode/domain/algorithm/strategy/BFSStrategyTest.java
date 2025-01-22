package com.coffeecode.domain.algorithm.strategy;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

class BFSStrategyTest {

    private BFSStrategy bfs;
    private RouteMap routeMap;
    private Locations loc1, loc2, loc3;

    @BeforeEach
    void setUp() {
        bfs = new BFSStrategy();
        routeMap = new RouteMap(new GeoToolsCalculator());

        loc1 = new Locations("A", 0, 0);
        loc2 = new Locations("B", 1, 1);
        loc3 = new Locations("C", 2, 2);

        routeMap.addLocation(loc1);
        routeMap.addLocation(loc2);
        routeMap.addLocation(loc3);
    }

    @Test
    void findPath_shouldReturnPath_whenExists() {
        List<Route> path = bfs.findPath(routeMap, loc1.id(), loc3.id());

        assertFalse(path.isEmpty());
        assertEquals(loc1.id(), path.get(0).sourceId());
        assertEquals(loc3.id(), path.get(path.size() - 1).targetId());
    }

    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        List<Route> path = bfs.findPath(routeMap, loc1.id(), UUID.randomUUID());
        assertTrue(path.isEmpty());
    }
}
