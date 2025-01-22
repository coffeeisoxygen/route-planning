package com.coffeecode.domain.algorithm.strategy;

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

import com.coffeecode.domain.algorithm.component.PathFinding;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Component
public class DFStrategy implements PathFinding {

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        Deque<UUID> stack = new ArrayDeque<>();
        Map<UUID, Route> pathParent = new HashMap<>();
        Set<UUID> visited = new HashSet<>();

        stack.push(source);
        visited.add(source);
        boolean foundTarget = false;

        while (!stack.isEmpty() && !foundTarget) {
            UUID current = stack.peek();

            if (current.equals(target)) {
                foundTarget = true;
                continue;
            }

            Optional<Route> nextRoute = map.getRoutes().stream()
                    .filter(r -> r.sourceId().equals(current))
                    .filter(r -> !visited.contains(r.targetId()))
                    .min((r1, r2) -> Double.compare(r1.distance(), r2.distance()));

            if (nextRoute.isPresent()) {
                UUID next = nextRoute.get().targetId();
                stack.push(next);
                visited.add(next);
                pathParent.put(next, nextRoute.get());
            } else {
                stack.pop();
            }
        }

        if (!foundTarget) {
            return Collections.emptyList();
        }

        return reconstructPath(pathParent, source, target);
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
    public String getAlgorithmName() {
        return "Depth First Search";
    }
}
