package com.coffeecode.domain.graph;

import java.util.*;

import com.coffeecode.domain.route.builder.RouteBuilder;
import com.coffeecode.domain.route.factory.RouteFactory;
import com.coffeecode.domain.route.model.*;
import com.coffeecode.domain.location.util.GeoToolsCalculator;
import com.coffeecode.domain.location.service.LocationMap;

public class GraphOperations {

    private final Graph graph;
    private final GeoToolsCalculator calculator;
    private final LocationMap locationMap;

    public GraphOperations(Graph graph, GeoToolsCalculator calculator, LocationMap locationMap) {
        this.graph = graph;
        this.calculator = calculator;
        this.locationMap = locationMap;
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

    public void addBidirectionalRoute(UUID source, UUID target) {
        double distance = calculateDistance(source, target);
        Route[] routes = RouteFactory.createBidirectional(source, target, distance, RouteType.DEFAULT);
        for (Route route : routes) {
            graph.addEdge(route);
        }
    }

    private double calculateDistance(UUID source, UUID target) {
        var sourceLocation = locationMap.getLocation(source)
                .orElseThrow(() -> new IllegalArgumentException("Source location not found"));
        var targetLocation = locationMap.getLocation(target)
                .orElseThrow(() -> new IllegalArgumentException("Target location not found"));

        return calculator.calculateDistance(
                sourceLocation.latitude(), sourceLocation.longitude(),
                targetLocation.latitude(), targetLocation.longitude()
        );
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

    public void clear() {
        graph.clear();
    }
}
