package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.api.PathFinding;
import com.coffeecode.domain.algorithm.component.PathFindingStats;
import com.coffeecode.domain.algorithm.result.PathStatistics;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Component
public class DFSStrategy implements PathFinding {

    private final PathFindingStats stats;

    public DFSStrategy() {
        this.stats = new PathFindingStats();
    }

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        stats.startTracking();

        Deque<UUID> stack = new ArrayDeque<>();
        Map<UUID, Route> pathParent = new HashMap<>();
        Set<UUID> visited = new HashSet<>();

        stack.push(source);
        visited.add(source);

        while (!stack.isEmpty()) {
            UUID current = stack.peek();
            stats.incrementVisited();

            if (current.equals(target)) {
                stats.stopTracking();
                return reconstructPath(pathParent, source, target);
            }

            Optional<Route> nextRoute = map.getActiveRoutes(current).stream()
                    .filter(r -> !visited.contains(r.targetId()))
                    .findFirst();

            if (nextRoute.isPresent()) {
                UUID next = nextRoute.get().targetId();
                stack.push(next);
                visited.add(next);
                pathParent.put(next, nextRoute.get());
            } else {
                stack.pop();
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
        return "Depth First Search";
    }
}
