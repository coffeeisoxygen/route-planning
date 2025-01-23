package com.coffeecode.domain.algorithm.core.pathfinding;

import java.util.*;
import com.coffeecode.domain.graph.Graph;
import com.coffeecode.domain.route.model.Route;

public class DFSStrategy extends AbstractPathFinding {

    @Override
    protected List<Route> executeFindPath(Graph graph, UUID source, UUID target) {
        Map<UUID, Route> pathParent = new HashMap<>();
        Set<UUID> visited = new HashSet<>();

        if (dfs(graph, source, target, visited, pathParent)) {
            return reconstructPath(pathParent, source, target);
        }

        return Collections.emptyList();
    }

    private boolean dfs(Graph graph, UUID current, UUID target,
            Set<UUID> visited, Map<UUID, Route> pathParent) {
        stats.incrementVisited();

        if (current.equals(target)) {
            return true;
        }

        visited.add(current);

        for (Route route : graph.getOutgoingEdges(current)) {
            UUID neighbor = route.targetId();
            if (!visited.contains(neighbor)) {
                pathParent.put(neighbor, route);
                if (dfs(graph, neighbor, target, visited, pathParent)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getStrategyName() {
        return "DFS";
    }

    private List<Route> reconstructPath(Map<UUID, Route> pathParent, UUID source, UUID target) {
        List<Route> path = new ArrayList<>();
        UUID current = target;

        while (pathParent.containsKey(current)) {
            Route route = pathParent.get(current);
            path.add(0, route);
            current = route.sourceId();
            if (current.equals(source)) {
                break;
            }
        }

        return path;
    }
}
