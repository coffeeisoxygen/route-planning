package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.api.PathFinding;
import com.coffeecode.domain.algorithm.api.SearchNode;
import com.coffeecode.domain.algorithm.result.PathFindingStats;
import com.coffeecode.domain.algorithm.result.ExecutionStatistics;
import com.coffeecode.domain.route.model.Route;
import com.coffeecode.domain.route.model.RouteMap;

@Component
public class DFSStrategy implements PathFinding {

    private final PathFindingStats stats;

    public DFSStrategy() {
        this.stats = new PathFindingStats();
    }

    private static class Node implements SearchNode {

        private final UUID id;
        private final int depth;

        Node(UUID id, int depth) {
            this.id = id;
            this.depth = depth;
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Override
        public double getScore() {
            return -depth;
        } // Inverse for DFS ordering
    }

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        stats.startTracking();
        Deque<Node> stack = new ArrayDeque<>();
        Map<UUID, Route> pathParent = new HashMap<>();
        Set<UUID> visited = new HashSet<>();

        stack.push(new Node(source, 0));

        while (!stack.isEmpty()) {
            Node current = stack.peek();
            stats.incrementVisited();

            // Handle circular path
            if (current.getId().equals(target)
                    && (!current.getId().equals(source) || !pathParent.isEmpty())) {
                stats.stopTracking();
                return reconstructPath(pathParent, source, target);
            }

            List<Route> nextRoutes = map.getActiveRoutes(current.getId()).stream()
                    .filter(r -> !visited.contains(r.targetId()) || r.targetId().equals(target))
                    .toList();

            if (nextRoutes.isEmpty()) {
                stack.pop();
                continue;
            }

            Route next = nextRoutes.get(0);
            stack.push(new Node(next.targetId(), current.depth + 1));
            visited.add(next.targetId());
            pathParent.put(next.targetId(), next);
        }

        stats.stopTracking();
        return Collections.emptyList();
    }

    private List<Route> reconstructPath(Map<UUID, Route> pathParent, UUID source, UUID target) {
        List<Route> path = new ArrayList<>();
        UUID current = target;

        // Changed: Handle circular paths
        if (source.equals(target)) {
            Route lastRoute = pathParent.get(source);
            if (lastRoute != null) {
                path.add(lastRoute);
                current = lastRoute.sourceId();
            }
        }

        while (!current.equals(source)) {
            Route route = pathParent.get(current);
            if (route == null) {
                break;
            }
            path.add(0, route);
            current = route.sourceId();
        }

        return path;
    }

    @Override
    public ExecutionStatistics getLastRunStatistics() {
        return stats.getLastRunStats();
    }

    @Override
    public String getAlgorithmName() {
        return "Depth First Search";
    }

}
