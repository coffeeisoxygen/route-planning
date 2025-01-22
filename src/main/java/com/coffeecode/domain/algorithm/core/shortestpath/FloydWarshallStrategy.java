package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.api.AllPairsShortestPath;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Component
public class FloydWarshallStrategy implements AllPairsShortestPath {

    private Map<UUID, Map<UUID, Double>> distances;
    private Map<UUID, Map<UUID, UUID>> next;
    private UUID source;

    @Override
    public Map<UUID, Map<UUID, Double>> getAllDistances() {
        return Collections.unmodifiableMap(distances);
    }

    @Override
    public Map<UUID, Map<UUID, UUID>> getNextHops() {
        return Collections.unmodifiableMap(next);
    }

    @Override
    public Map<UUID, Double> getDistances() {
        return Collections.unmodifiableMap(distances.get(source));
    }

    @Override
    public double getPathCost(UUID target) {
        return distances.get(source).getOrDefault(target, Double.POSITIVE_INFINITY);
    }

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        // Initialize distance and next matrices
        initializeMatrices(map);

        // Get all vertices
        List<UUID> vertices = new ArrayList<>(distances.keySet());

        // Floyd-Warshall algorithm
        for (UUID k : vertices) {
            for (UUID i : vertices) {
                for (UUID j : vertices) {
                    double directDist = distances.get(i).getOrDefault(j, Double.POSITIVE_INFINITY);
                    double pathDist = distances.get(i).getOrDefault(k, Double.POSITIVE_INFINITY)
                            + distances.get(k).getOrDefault(j, Double.POSITIVE_INFINITY);

                    if (pathDist < directDist) {
                        distances.get(i).put(j, pathDist);
                        next.get(i).put(j, next.get(i).get(k));
                    }
                }
            }
        }

        // Reconstruct path
        return reconstructPath(map, source, target);
    }

    private void initializeMatrices(RouteMap map) {
        distances = new HashMap<>();
        next = new HashMap<>();

        // Initialize with infinity
        for (var loc : map.getLocations()) {
            distances.put(loc.id(), new HashMap<>());
            next.put(loc.id(), new HashMap<>());
            for (var otherLoc : map.getLocations()) {
                distances.get(loc.id()).put(otherLoc.id(), Double.POSITIVE_INFINITY);
                next.get(loc.id()).put(otherLoc.id(), null);
            }
        }

        // Set direct routes
        for (Route route : map.getRoutes()) {
            distances.get(route.sourceId()).put(route.targetId(), route.distance());
            next.get(route.sourceId()).put(route.targetId(), route.targetId());
        }

        // Set self-distances to 0
        for (UUID id : distances.keySet()) {
            distances.get(id).put(id, 0.0);
            next.get(id).put(id, id);
        }
    }

    private List<Route> reconstructPath(RouteMap map, UUID source, UUID target) {
        if (!next.containsKey(source) || !next.get(source).containsKey(target)) {
            return Collections.emptyList();
        }

        List<Route> path = new ArrayList<>();
        UUID current = source;

        while (!current.equals(target)) {
            UUID nextVertex = next.get(current).get(target);
            map.getRoute(current, nextVertex).ifPresent(path::add);
            current = nextVertex;
        }

        return path;
    }

    @Override
    public String getAlgorithmName() {
        return "Floyd-Warshall Algorithm";
    }
}
