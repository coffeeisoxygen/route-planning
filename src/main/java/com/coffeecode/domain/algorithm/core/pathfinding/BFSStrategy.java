package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.api.PathFinding;
import com.coffeecode.domain.algorithm.api.SearchNode;
import com.coffeecode.domain.algorithm.result.PathFindingStats;
import com.coffeecode.domain.algorithm.result.ExecutionStatistics;
import com.coffeecode.domain.route.RouteMap;
import com.coffeecode.domain.route.model.Route;

@Component
public class BFSStrategy implements PathFinding {

    private final PathFindingStats stats;

    public BFSStrategy() {
        this.stats = new PathFindingStats();
    }

    private static class Node implements SearchNode {

        private final UUID id;
        private final int depth;  // For level tracking

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
            return depth;
        }
    }

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        stats.startTracking();
        Queue<Node> queue = new LinkedList<>();
        Map<UUID, Route> pathParent = new HashMap<>();
        Set<UUID> visited = new HashSet<>();

        queue.offer(new Node(source, 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            stats.incrementVisited();

            // Handle circular path
            if (current.getId().equals(target)
                    && (!current.getId().equals(source) || !pathParent.isEmpty())) {
                stats.stopTracking();
                return reconstructPath(pathParent, source, target);
            }

            for (Route route : map.getActiveRoutes(current.getId())) {
                UUID nextId = route.targetId();
                // Allow revisit of target for circular paths
                if (!visited.contains(nextId) || nextId.equals(target)) {
                    queue.offer(new Node(nextId, current.depth + 1));
                    visited.add(nextId);
                    pathParent.put(nextId, route);
                }
            }
        }

        stats.stopTracking();
        return Collections.emptyList();
    }

    private List<Route> reconstructPath(Map<UUID, Route> pathParent, UUID source, UUID target) {
        List<Route> path = new ArrayList<>();
        UUID current = target;

        // Changed: Allow reconstruction when source equals target
        if (source.equals(target)) {
            // Get last route that leads back to source
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
        return "Breadth First Search";
    }

}
