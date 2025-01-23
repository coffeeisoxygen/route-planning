package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.*;
import com.coffeecode.domain.graph.Graph;
import com.coffeecode.domain.route.model.Route;

public class BFSStrategy extends AbstractPathFinding {
    @Override
    protected List<Route> executeFindPath(Graph graph, UUID source, UUID target) {
        Queue<UUID> queue = new LinkedList<>();
        Map<UUID, Route> pathParent = new HashMap<>();
        Set<UUID> visited = new HashSet<>();

        queue.offer(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            UUID current = queue.poll();
            stats.incrementVisited();

            if (current.equals(target)) {
                return reconstructPath(pathParent, source, target);
            }

            for (Route route : graph.getOutgoingEdges(current)) {
                UUID neighbor = route.targetId();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    pathParent.put(neighbor, route);
                    queue.offer(neighbor);
                }
            }
        }

        return Collections.emptyList();
    }

    @Override
    public String getStrategyName() {
        return "BFS";
    }

    private List<Route> reconstructPath(Map<UUID, Route> pathParent, UUID source, UUID target) {
        List<Route> path = new ArrayList<>();
        UUID current = target;

        while (pathParent.containsKey(current)) {
            Route route = pathParent.get(current);
            path.add(0, route);
            current = route.sourceId();
            if (current.equals(source)) break;
        }

        return path;
    }
}
