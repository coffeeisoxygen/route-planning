package com.coffeecode.domain.algorithm.shortestpath;

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

import com.coffeecode.domain.algorithm.component.ShortestPathFinding;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Component
public class DijkstraStrategy implements ShortestPathFinding {

    private Map<UUID, Double> distances;
    private Map<UUID, UUID> predecessors;
    private Map<UUID, Route> pathParent;

    private static class Node implements Comparable<Node> {

        UUID id;
        double distance;

        Node(UUID id, double distance) {
            this.id = id;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(distance, other.distance);
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
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        distances = new HashMap<>();
        pathParent = new HashMap<>();
        predecessors = new HashMap<>();
        Set<UUID> visited = new HashSet<>();

        queue.offer(new Node(source, 0));
        distances.put(source, 0.0);
        predecessors.put(source, source);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.id.equals(target)) {
                return reconstructPath(pathParent, source, target);
            }

            if (visited.contains(current.id)) {
                continue;
            }
            visited.add(current.id);

            for (Route route : map.getRoutes()) {
                if (!route.sourceId().equals(current.id)) {
                    continue;
                }

                double newDistance = distances.get(current.id) + route.distance();
                UUID neighbor = route.targetId();

                if (!distances.containsKey(neighbor) || newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, current.id);
                    pathParent.put(neighbor, route);
                    queue.offer(new Node(neighbor, newDistance));
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Route> reconstructPath(Map<UUID, Route> pathParent, UUID source, UUID target) {
        List<Route> path = new ArrayList<>();
        UUID current = target;

        while (!current.equals(source)) {
            Route route = pathParent.get(current);
            path.add(0, route);
            current = route.sourceId();
        }

        return path;
    }

    @Override
    public Map<UUID, Double> getDistances() {
        return Collections.unmodifiableMap(distances);
    }

    @Override
    public Map<UUID, UUID> getPath() {
        return Collections.unmodifiableMap(predecessors);
    }

    @Override
    public double getPathCost(UUID target) {
        return distances.getOrDefault(target, Double.POSITIVE_INFINITY);
    }

    @Override
    public String getAlgorithmName() {
        return "Dijkstra's Algorithm";
    }
}
