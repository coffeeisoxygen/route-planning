package com.coffeecode.domain.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.component.PathFinding;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Component
public class FloydWarshallStrategy implements PathFinding {

    private Map<UUID, Map<UUID, Double>> distances;
    private Map<UUID, Map<UUID, UUID>> next;

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
