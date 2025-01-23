package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.graph.Graph;
import com.coffeecode.domain.route.model.Route;
import com.coffeecode.domain.route.model.RouteMetadata;
import com.coffeecode.domain.route.model.RouteStatus;
import com.coffeecode.domain.route.model.RouteType;

class PathFindingTest {

    private Graph graph;
    private UUID nodeA, nodeB, nodeC, nodeD;
    private BFSStrategy bfs;
    private DFSStrategy dfs;

    @BeforeEach
    void setUp() {
        graph = new Graph();

        // Create test nodes
        nodeA = UUID.randomUUID();
        nodeB = UUID.randomUUID();
        nodeC = UUID.randomUUID();
        nodeD = UUID.randomUUID();

        // Create test routes
        graph.addEdge(new Route(nodeA, nodeB, 1.0, 1.0, RouteStatus.ACTIVE, new RouteMetadata(0L, 0L, ""), RouteType.DEFAULT));
        graph.addEdge(new Route(nodeB, nodeC, 1.0, 1.0, RouteStatus.ACTIVE, new RouteMetadata(0L, 0L, ""), RouteType.DEFAULT));
        graph.addEdge(new Route(nodeC, nodeD, 1.0, 1.0, RouteStatus.ACTIVE, new RouteMetadata(0L, 0L, ""), RouteType.DEFAULT));
        graph.addEdge(new Route(nodeA, nodeC, 2.0, 2.0, RouteStatus.ACTIVE, new RouteMetadata(0L, 0L, ""), RouteType.DEFAULT));

        bfs = new BFSStrategy();
        dfs = new DFSStrategy();
    }

    @Test
    void testBFSFindPath() {
        var result = bfs.findPath(graph, nodeA, nodeD);
        assertFalse(result.path().isEmpty(), "BFS should find a path");
        assertEquals("BFS", result.algorithmName());
        assertTrue(result.stats().visitedNodes() > 0);
    }

    @Test
    void testDFSFindPath() {
        var result = dfs.findPath(graph, nodeA, nodeD);
        assertFalse(result.path().isEmpty(), "DFS should find a path");
        assertEquals("DFS", result.algorithmName());
        assertTrue(result.stats().visitedNodes() > 0);
    }
}
