package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.api.SearchNode;
import com.coffeecode.domain.algorithm.api.SingleSourceShortestPath;
import com.coffeecode.domain.algorithm.component.PathFindingStats;
import com.coffeecode.domain.algorithm.result.PathStatistics;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Component
public class DijkstraStrategy implements SingleSourceShortestPath {

    private final PathFindingStats stats;
    private UUID source;
    private Map<UUID, Double> distances;
    private Map<UUID, UUID> predecessors;
    private Map<UUID, Route> pathParent;

    public DijkstraStrategy() {
        this.stats = new PathFindingStats();
    }

    public UUID getSource() {
        return source;
    }

    private static class Node implements SearchNode {

        private final UUID id;
        private final double distance;

        Node(UUID id, double distance) {
            this.id = id;
            this.distance = distance;
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Override
        public double getScore() {
            return distance;
        }
    }

    @Override
    public void initialize(UUID source) {
        this.source = source;
        this.distances = new HashMap<>();
        this.predecessors = new HashMap<>();
        this.pathParent = new HashMap<>();
    }

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        stats.startTracking();
        initialize(source);

        PriorityQueue<Node> queue = new PriorityQueue<>();
        Set<UUID> visited = new HashSet<>();

        queue.offer(new Node(source, 0));
        distances.put(source, 0.0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            stats.incrementVisited();

            if (current.id.equals(target)) {
                stats.stopTracking();
                return reconstructPath(pathParent, source, target);
            }

            if (visited.contains(current.id)) {
                continue;
            }
            visited.add(current.id);

            for (Route route : map.getActiveRoutes(current.id)) {
                UUID neighbor = route.targetId();
                double newDistance = distances.get(current.id) + route.weight();

                distances.computeIfAbsent(neighbor, k -> {
                    pathParent.put(neighbor, route);
                    queue.offer(new Node(neighbor, newDistance));
                    return newDistance;
                });
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    pathParent.put(neighbor, route);
                    queue.offer(new Node(neighbor, newDistance));
                }
            }
        }

        stats.stopTracking();
        return Collections.emptyList();
    }

    private List<Route> reconstructPath(Map<UUID, Route> pathParent, UUID source, UUID target) {
        List<Route> path = new ArrayList<>();
        UUID current = target;

        while (!current.equals(source)) {
            Route route = pathParent.get(current);
            if (route == null) {
                break;
            }
            path.add(0, route);
            current = route.sourceId();
        }

        return path;
    }

    @Override
    public Map<UUID, UUID> getPredecessors() {
        return Collections.unmodifiableMap(predecessors);
    }

    @Override
    public Map<UUID, Double> getDistances() {
        return Collections.unmodifiableMap(distances);
    }

    @Override
    public double getPathCost(UUID target) {
        return distances.getOrDefault(target, Double.POSITIVE_INFINITY);
    }

    @Override
    public String getAlgorithmName() {
        return "Dijkstra's Algorithm";
    }

    @Override
    public PathStatistics getLastRunStatistics() {
        return stats.getLastRunStats();
    }
}
