package com.coffeecode.domain.algorithm.strategy;

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

        while (!stack.isEmpty()) {
            UUID current = stack.pop();

            if (current.equals(target)) {
                return reconstructPath(pathParent, source, target);
            }

            for (Route route : map.getRoutes()) {
                if (route.sourceId().equals(current) && !visited.contains(route.targetId())) {
                    stack.push(route.targetId());
                    visited.add(route.targetId());
                    pathParent.put(route.targetId(), route);
                }
                if (route.targetId().equals(current) && !visited.contains(route.sourceId())) {
                    stack.push(route.sourceId());
                    visited.add(route.sourceId());
                    pathParent.put(route.sourceId(), route);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Route> reconstructPath(Map<UUID, Route> pathParent, UUID source, UUID target) {
        List<Route> path = new ArrayList<>();
        UUID current = target;

        while (!current.equals(source)) {
            Route route = pathParent.get(current);
            path.add(0, route);
            if (route.sourceId().equals(current)) {
                current = route.targetId();
            } else {
                current = route.sourceId();
            }
        }

        return path;
    }

    @Override
    public String getAlgorithmName() {
        return "Depth First Search";
    }
}
