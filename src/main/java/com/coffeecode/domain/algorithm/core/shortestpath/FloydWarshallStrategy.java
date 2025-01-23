package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.api.AllPairsShortestPath;
import com.coffeecode.domain.algorithm.result.PathFindingStats;
import com.coffeecode.domain.location.model.Locations;
import com.coffeecode.domain.algorithm.result.ExecutionStatistics;
import com.coffeecode.domain.route.model.Route;
import com.coffeecode.domain.route.model.RouteMap;

@Component
public class FloydWarshallStrategy implements AllPairsShortestPath {

    private final PathFindingStats stats;
    private Map<UUID, Map<UUID, Double>> distances;
    private Map<UUID, Map<UUID, UUID>> next;
    private Set<UUID> vertices;
    private UUID source;

    public FloydWarshallStrategy() {
        this.stats = new PathFindingStats();
    }

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        stats.startTracking();
        this.source = source;
        initializeMatrices(map);

        for (UUID k : vertices) {
            for (UUID i : vertices) {
                for (UUID j : vertices) {
                    stats.incrementVisited();
                    updateDistance(i, j, k);
                }
            }
        }

        stats.stopTracking();
        return reconstructPath(map, source, target);
    }

    private void initializeMatrices(RouteMap map) {
        distances = new HashMap<>();
        next = new HashMap<>();
        vertices = map.getLocations().stream()
                .map(Locations::id)
                .collect(Collectors.toSet());

        for (UUID id : vertices) {
            distances.put(id, new HashMap<>());
            next.put(id, new HashMap<>());

            for (UUID otherId : vertices) {
                if (id.equals(otherId)) {
                    distances.get(id).put(otherId, 0.0);
                    next.get(id).put(otherId, id);
                } else {
                    distances.get(id).put(otherId, Double.POSITIVE_INFINITY);
                    next.get(id).put(otherId, null);
                }
            }
        }

        for (UUID id : vertices) {
            for (Route route : map.getRoutesFrom(id)) {
                if (route.isActive()) {
                    distances.get(id).put(route.targetId(), route.distance());
                    next.get(id).put(route.targetId(), route.targetId());
                }
            }
        }
    }

    private void updateDistance(UUID i, UUID j, UUID k) {
        double directDist = distances.get(i).getOrDefault(j, Double.POSITIVE_INFINITY);
        double pathDist = distances.get(i).getOrDefault(k, Double.POSITIVE_INFINITY)
                + distances.get(k).getOrDefault(j, Double.POSITIVE_INFINITY);

        if (pathDist < directDist) {
            distances.get(i).put(j, pathDist);
            next.get(i).put(j, next.get(i).get(k));
        }
    }

    private List<Route> reconstructPath(RouteMap map, UUID source, UUID target) {
        if (!hasPath(source, target)) {
            return Collections.emptyList();
        }

        List<Route> path = new ArrayList<>();
        UUID current = source;

        while (!current.equals(target)) {
            UUID nextVertex = next.get(current).get(target);
            if (nextVertex == null) {
                break;
            }

            Optional<Route> route = map.getRoute(current, nextVertex);
            if (route.isEmpty()) {
                break;
            }

            path.add(route.get());
            current = nextVertex;
        }

        return path;
    }

    private boolean hasPath(UUID source, UUID target) {
        return distances.containsKey(source)
                && distances.get(source).containsKey(target)
                && distances.get(source).get(target) < Double.POSITIVE_INFINITY;
    }

    @Override
    public Map<UUID, Map<UUID, Double>> getAllDistances() {
        return Collections.unmodifiableMap(distances);
    }

    @Override
    public Map<UUID, Map<UUID, UUID>> getNextHops() {
        return Collections.unmodifiableMap(next);
    }

    @Override
    public ExecutionStatistics getLastRunStatistics() {
        return stats.getLastRunStats();
    }

    @Override
    public Map<UUID, Double> getDistances() {
        return distances.getOrDefault(source, Collections.emptyMap());
    }

    @Override
    public double getPathCost(UUID target) {
        return distances.getOrDefault(source, Collections.emptyMap())
                .getOrDefault(target, Double.POSITIVE_INFINITY);
    }

    @Override
    public String getAlgorithmName() {
        return "Floyd-Warshall";
    }
}
