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
import com.coffeecode.domain.route.model.Route;
import com.coffeecode.domain.route.model.RouteMap;

@Component
public class AStarStrategy implements SingleSourceShortestPath {

    private final PathFindingStats stats;
    private UUID source;
    private Map<UUID, Double> gScore;
    private Map<UUID, Route> pathParent;
    private Map<UUID, UUID> predecessors = new HashMap<>();

    public AStarStrategy() {
        this.stats = new PathFindingStats();
    }

    public UUID getSource() {
        return source;
    }

    private static class Node implements SearchNode {

        private final UUID id;
        private final double gScore;
        private final double fScore;

        Node(UUID id, double gScore, double fScore) {
            this.id = id;
            this.gScore = gScore;
            this.fScore = fScore;
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Override
        public double getScore() {
            return fScore;
        }

        public double getGScore() {
            return gScore;
        }
    }

    @Override
    public void initialize(UUID source) {
        this.source = source;
        this.gScore = new HashMap<>();
        this.pathParent = new HashMap<>();
        this.gScore.computeIfAbsent(source, k -> 0.0);
    }

    private double heuristic(RouteMap map, UUID current, UUID target) {
        // Euclidean distance heuristic
        return map.calculateDirectDistance(current, target);
    }

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        stats.startTracking();
        initialize(source);

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<UUID> closedSet = new HashSet<>();

        openSet.offer(new Node(source, 0, heuristic(map, source, target)));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            stats.incrementVisited();

            if (current.id.equals(target)) {
                stats.stopTracking();
                return reconstructPath(pathParent, source, target);
            }

            closedSet.add(current.id);

            for (Route route : map.getActiveRoutes(current.id)) {
                UUID neighbor = route.targetId();
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeG = gScore.getOrDefault(current.id, Double.POSITIVE_INFINITY) + route.weight();

                if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    pathParent.put(neighbor, route);
                    predecessors.put(neighbor, current.id);
                    double f = tentativeG + heuristic(map, neighbor, target);
                    openSet.offer(new Node(neighbor, tentativeG, f));
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
        return Collections.unmodifiableMap(gScore);
    }

    @Override
    public double getPathCost(UUID target) {
        return gScore.getOrDefault(target, Double.POSITIVE_INFINITY);
    }

    @Override
    public String getAlgorithmName() {
        return "A* Search";
    }

    @Override
    public ExecutionStatistics getLastRunStatistics() {
        return stats.getLastRunStats();
    }
}
