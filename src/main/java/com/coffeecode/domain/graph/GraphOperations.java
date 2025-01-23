package com.coffeecode.domain.graph;

import java.util.*;

import com.coffeecode.domain.route.builder.RouteBuilder;
import com.coffeecode.domain.route.model.*;

public class GraphOperations {

    private final Graph graph;

    public GraphOperations(Graph graph) {
        this.graph = graph;
    }

    // Core Graph Operations
    public List<Route> getRoutesFrom(UUID source) {
        return graph.getOutgoingEdges(source);
    }

    public List<Route> getOutgoingRoutes(UUID source) {
        return graph.getOutgoingEdges(source);
    }

    public List<Route> getIncomingRoutes(UUID target) {
        return graph.getIncomingEdges(target);
    }

    public List<Route> getNeighbors(UUID locationId) {
        return graph.getOutgoingEdges(locationId);
    }

    // Route Management
    public Optional<Route> getRoute(UUID source, UUID target) {
        return graph.getEdge(source, target);
    }

    public void addWeightedRoute(UUID source, UUID target, double weight, RouteType type) {
        Route route = new RouteBuilder()
                .sourceId(source)
                .targetId(target)
                .weight(weight)
                .type(type)
                .build();
        graph.addEdge(route);
    }

    public void removeRoute(UUID source, UUID target) {
        graph.removeEdge(source, target);
    }

    public boolean hasRoute(UUID source, UUID target) {
        return graph.hasEdge(source, target);
    }

    // Path Operations
    public boolean isValidPath(List<Route> path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        for (int i = 0; i < path.size() - 1; i++) {
            Route current = path.get(i);
            Route next = path.get(i + 1);
            if (!current.targetId().equals(next.sourceId())) {
                return false;
            }
        }
        return true;
    }

    public double calculatePathWeight(List<Route> path) {
        return path.stream()
                .filter(r -> r.status() == RouteStatus.ACTIVE)
                .mapToDouble(Route::weight)
                .sum();
    }

    public Collection<Route> getRoutes() {
        return graph.getAllEdges();
    }

    public Optional<Route> findMinWeightRoute(UUID source, UUID target) {
        return graph.getOutgoingEdges(source).stream()
                .filter(r -> r.targetId().equals(target))
                .min(Comparator.comparingDouble(Route::weight));
    }
}
