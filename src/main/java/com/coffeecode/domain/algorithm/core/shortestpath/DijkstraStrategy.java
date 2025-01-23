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
import com.coffeecode.domain.algorithm.result.PathFindingStats;
import com.coffeecode.domain.algorithm.result.ExecutionStatistics;
import com.coffeecode.domain.route.RouteMap;
import com.coffeecode.domain.route.model.Route;

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

        PriorityQueue<SearchNode> queue = new PriorityQueue<>();
        Set<UUID> visited = new HashSet<>();

        queue.offer(new Node(source, 0));
        distances.put(source, 0.0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            stats.incrementVisited();

            // Don't skip target node when finding circular paths
            if (!visited.contains(current.id) || current.id.equals(target)) {
                // Only add to visited if not the target (allows revisiting for circular paths)
                if (!current.id.equals(target)) {
                    visited.add(current.id);
                }

                for (Route route : map.getActiveRoutes(current.id)) {
                    UUID neighbor = route.targetId();
                    double newDistance = distances.get(current.id) + route.distance();

                    if (!distances.containsKey(neighbor)
                            || newDistance < distances.get(neighbor)) {

                        distances.put(neighbor, newDistance);
                        pathParent.put(neighbor, route);
                        predecessors.put(neighbor, current.id);
                        queue.offer(new Node(neighbor, newDistance));
                    }
                }
            }

            // For non-circular paths, can return when target is found
            if (current.id.equals(target) && !source.equals(target)) {
                stats.stopTracking();
                return reconstructPath(pathParent, source, target);
            }
        }

        stats.stopTracking();

        // For circular paths, reconstruct if we found any path back to source
        if (source.equals(target) && pathParent.containsKey(target)) {
            return reconstructPath(pathParent, source, target);
        }

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

    private boolean isValidCircularPath(List<Route> path, UUID source) {
        if (path.isEmpty()) {
            return false;
        }

        // Verify path starts and ends at source
        UUID firstNode = path.get(0).sourceId();
        UUID lastNode = path.get(path.size() - 1).targetId();

        return firstNode.equals(source) && lastNode.equals(source);
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
    public ExecutionStatistics getLastRunStatistics() {
        return stats.getLastRunStats();
    }
}
