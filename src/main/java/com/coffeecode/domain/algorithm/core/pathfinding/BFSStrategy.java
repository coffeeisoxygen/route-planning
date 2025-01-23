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
import com.coffeecode.domain.algorithm.component.PathFindingStats;
import com.coffeecode.domain.algorithm.result.PathStatistics;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Component
public class BFSStrategy implements PathFinding {

    private final PathFindingStats stats;

    public BFSStrategy() {
        this.stats = new PathFindingStats();
    }

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        stats.startTracking();

        Queue<UUID> queue = new LinkedList<>();
        Map<UUID, Route> pathParent = new HashMap<>();
        Set<UUID> visited = new HashSet<>();

        queue.offer(source);

        while (!queue.isEmpty()) {
            UUID current = queue.poll();
            stats.incrementVisited();

            if (current.equals(target)) {
                stats.stopTracking();
                return reconstructPath(pathParent, source, target);
            }

            for (Route route : map.getActiveRoutes(current)) {
                if (!visited.contains(route.targetId())) {
                    queue.offer(route.targetId());
                    visited.add(route.targetId());
                    pathParent.put(route.targetId(), route);
                }
            }
        }

        stats.stopTracking();
        return Collections.emptyList();
    }

    private List<Route> reconstructPath(Map<UUID, Route> pathParent, UUID source, UUID target) {
        List<Route> path = new ArrayList<>();
        UUID current = target;

        while (!current.equals(source)) {
            Route route = pathParent.get(current);
            path.add(0, route);
            current = route.sourceId();
        }

        return path;
    }

    @Override
    public PathStatistics getLastRunStatistics() {
        return stats.getLastRunStats();
    }

    @Override
    public String getAlgorithmName() {
        return "Breadth First Search";
    }
}
