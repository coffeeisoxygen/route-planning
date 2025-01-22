package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.coffeecode.domain.algorithm.api.PathFinding;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;

@Component
public class AStarStrategy implements PathFinding {

    private static class Node implements Comparable<Node> {

        UUID id;
        double gScore; // Cost from start
        double fScore; // Estimated total cost

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
    public List<Route> findPath(RouteMap map, UUID source, UUID target) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<UUID, Double> gScore = new HashMap<>();
        Map<UUID, Route> pathParent = new HashMap<>();

        // Initialize with start node
        openSet.offer(new Node(source, 0, heuristic(map, source, target)));
        gScore.put(source, 0.0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.id.equals(target)) {
                return reconstructPath(pathParent, source, target);
            }

            for (Route route : map.getRoutes()) {
                if (!route.sourceId().equals(current.id)) {
                    continue;
                }

                double tentativeGScore = gScore.get(current.id) + route.distance();
                UUID neighbor = route.targetId();

                gScore.computeIfAbsent(neighbor, k -> Double.MAX_VALUE);
                if (tentativeGScore < gScore.get(neighbor)) {
                    pathParent.put(neighbor, route);
                    gScore.put(neighbor, tentativeGScore);
                    double fScore = tentativeGScore + heuristic(map, neighbor, target);
                    openSet.offer(new Node(neighbor, tentativeGScore, fScore));
                }
            }
        }

        return Collections.emptyList();
    }

    private double heuristic(RouteMap map, UUID current, UUID target) {
        return map.calculateDirectDistance(current, target);
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
    public String getAlgorithmName() {
        return "A* Search";
    }
}
