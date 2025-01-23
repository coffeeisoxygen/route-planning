package com.coffeecode.domain.algorithm.strategy;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.algorithm.core.pathfinding.BFSStrategy;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.Route.RouteType;

class BFSStrategyTest extends BasePathFindingTest {

    private BFSStrategy bfs;

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        bfs = new BFSStrategy();

        // Populate routeMap with locations and routes
        routeMap.addLocation(testLocations.get("A"));
        routeMap.addLocation(testLocations.get("B"));
        routeMap.addLocation(testLocations.get("C"));
        routeMap.addLocation(testLocations.get("D"));
        routeMap.addLocation(testLocations.get("E"));
        routeMap.addLocation(testLocations.get("F"));
        routeMap.addLocation(testLocations.get("G"));
        routeMap.addLocation(testLocations.get("H"));
        routeMap.addLocation(testLocations.get("I"));

        routeMap.addRoute(testLocations.get("A").id(), testLocations.get("B").id(), RouteType.DIRECT);
        routeMap.addRoute(testLocations.get("B").id(), testLocations.get("C").id(), RouteType.DIRECT);
        routeMap.addRoute(testLocations.get("C").id(), testLocations.get("D").id(), RouteType.DIRECT);
        routeMap.addRoute(testLocations.get("D").id(), testLocations.get("E").id(), RouteType.DIRECT);
        routeMap.addRoute(testLocations.get("E").id(), testLocations.get("F").id(), RouteType.DIRECT);
        routeMap.addRoute(testLocations.get("F").id(), testLocations.get("G").id(), RouteType.DIRECT);
        routeMap.addRoute(testLocations.get("G").id(), testLocations.get("H").id(), RouteType.DIRECT);
        routeMap.addRoute(testLocations.get("H").id(), testLocations.get("I").id(), RouteType.DIRECT);
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
