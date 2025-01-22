package com.coffeecode.domain.algorithm.strategy;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Route;

class DFStrategyTest extends BasePathFindingTest {

    private DFStrategy dfs = new DFStrategy();

    @Test
    void findPath_shouldFindValidPath() {
        List<Route> path = dfs.findPath(routeMap, bandung.id(), surabaya.id());

        assertFalse(path.isEmpty());
        assertEquals(bandung.id(), path.get(0).sourceId());
        assertEquals(surabaya.id(), path.get(path.size() - 1).targetId());
    }

    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        List<Route> path = dfs.findPath(routeMap, bandung.id(), UUID.randomUUID());
        assertTrue(path.isEmpty());
    }

    @Test
    void findPath_shouldFindPath_throughIntermediate() {
        List<Route> path = dfs.findPath(routeMap, bandung.id(), surabaya.id());

        assertFalse(path.isEmpty());
        assertTrue(path.stream()
                .anyMatch(r -> r.sourceId().equals(bandung.id())
                || r.targetId().equals(surabaya.id())));
    }
}
