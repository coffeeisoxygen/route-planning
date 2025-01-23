package com.coffeecode.domain.algorithm.core.pathfinding;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.coffeecode.domain.graph.*;
import com.coffeecode.domain.route.model.*;
import java.util.*;

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
        graph.addEdge(new Route(nodeA, nodeB, 1.0, 0.0, RouteStatus.ACTIVE, new RouteMetadata(), RouteType.DEFAULT));
        graph.addEdge(new Route(nodeB, nodeC, 1.0, 0.0, RouteStatus.ACTIVE, new RouteMetadata(), RouteType.DEFAULT));
        graph.addEdge(new Route(nodeC, nodeD, 1.0, 0.0, RouteStatus.ACTIVE, new RouteMetadata(), RouteType.DEFAULT));
        graph.addEdge(new Route(nodeA, nodeC, 2.0, 0.0, RouteStatus.ACTIVE, new RouteMetadata(), RouteType.DEFAULT));

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
