package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.api.AllPairsShortestPath;
import com.coffeecode.domain.model.Locations;
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
        this.source = source; // Track source for getDistances()
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
        // Clear existing data
        distances = new HashMap<>();
        next = new HashMap<>();
        
        // Get unique locations
        Set<UUID> vertices = map.getLocations().stream()
            .map(Locations::id)
            .collect(Collectors.toSet());
            
        // Initialize matrices
        vertices.forEach(id -> {
            distances.put(id, new HashMap<>());
            next.put(id, new HashMap<>());
            
            vertices.forEach(otherId -> {
                distances.get(id).put(otherId, Double.POSITIVE_INFINITY);
                next.get(id).put(otherId, null);
            });
            
            // Set self-distance
            distances.get(id).put(id, 0.0);
            next.get(id).put(id, id);
        });
        
        // Set direct routes
        map.getRoutes().forEach(route -> {
            distances.get(route.sourceId()).put(route.targetId(), route.distance());
            next.get(route.sourceId()).put(route.targetId(), route.targetId());
        });
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
