package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.api.SingleSourceShortestPath;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Component
public class AStarStrategy implements SingleSourceShortestPath {

    private UUID source;
    private Map<UUID, Double> gScore;
    private Map<UUID, UUID> predecessors;
    private Map<UUID, Route> pathParent;

    public UUID getSource() {
        return source;
    }

    private static class Node implements Comparable<Node> {

        UUID id;
        double gScore;
        double fScore;

        Node(UUID id, double gScore, double fScore) {
            this.id = id;
            this.gScore = gScore;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(fScore, other.fScore);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Node node = (Node) obj;
            return Objects.equals(id, node.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

    }

    @Override
    public void initialize(UUID source) {
        this.source = source;
        this.gScore = new HashMap<>();
        this.gScore.put(source, 0.0); // Initialize gScore for the source node
        this.predecessors = new HashMap<>();
        this.pathParent = new HashMap<>();
    }

    @Override
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        initialize(source);
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<UUID> closedSet = new HashSet<>();

        openSet.offer(new Node(source, 0, heuristic(map, source, target)));
        gScore.put(source, 0.0);
        predecessors.put(source, source);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.id.equals(target)) {
                return reconstructPath(pathParent, source, target);
            }

            closedSet.add(current.id);
            processNeighbors(map, openSet, closedSet, current, target);
        }

        return Collections.emptyList();
    }

    private void processNeighbors(RouteMap map, PriorityQueue<Node> openSet, Set<UUID> closedSet, Node current, UUID target) {
        for (Route route : map.getRoutes()) {
            if (route.sourceId().equals(current.id)) {
                UUID neighbor = route.targetId();
                if (!closedSet.contains(neighbor)) {
                    double tentativeGScore = gScore.get(current.id) + route.distance();
    
                    gScore.computeIfAbsent(neighbor, k -> tentativeGScore);
                    if (tentativeGScore < gScore.get(neighbor)) {
                        gScore.put(neighbor, tentativeGScore);
                        predecessors.put(neighbor, current.id);
                        pathParent.put(neighbor, route);
                        double fScore = tentativeGScore + heuristic(map, neighbor, target);
                        Node neighborNode = new Node(neighbor, tentativeGScore, fScore);
                        if (!openSet.contains(neighborNode)) {
                            openSet.offer(neighborNode);
                        }
                    }
                }
            }
        }
        closedSet.add(current.id);
    }

    private double heuristic(RouteMap map, UUID current, UUID target) {
        return map.calculateDirectDistance(current, target);
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
}
