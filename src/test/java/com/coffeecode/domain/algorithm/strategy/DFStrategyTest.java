package com.coffeecode.domain.algorithm.strategy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
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
        Locations start = testLocations.get("A");
        Locations end = testLocations.get("I");

        List<Route> path = dfs.findPath(routeMap, start.id(), end.id());
        printRoutes(path);

        assertFalse(path.isEmpty());
        assertEquals(start.id(), path.get(0).sourceId());
        assertEquals(end.id(), path.get(path.size() - 1).targetId());
    }
}
