package com.coffeecode.domain.graph;

import java.util.*;
import com.coffeecode.domain.route.model.Route;

public class Graph {

    private final Map<UUID, Map<UUID, Route>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addEdge(Route route) {
        adjacencyList.computeIfAbsent(route.sourceId(), k -> new HashMap<>())
                .put(route.targetId(), route);
    }

    public Optional<Route> getEdge(UUID source, UUID target) {
        return Optional.ofNullable(adjacencyList.get(source))
                .map(edges -> edges.get(target));
    }

    public List<Route> getOutgoingEdges(UUID source) {
        return adjacencyList.getOrDefault(source, Collections.emptyMap())
                .values()
                .stream()
                .toList();
    }

    public List<Route> getIncomingEdges(UUID target) {
        return adjacencyList.entrySet().stream()
                .flatMap(e -> e.getValue().values().stream())
                .filter(r -> r.targetId().equals(target))
                .toList();
    }

    public void removeEdge(UUID source, UUID target) {
        Optional.ofNullable(adjacencyList.get(source))
                .ifPresent(edges -> edges.remove(target));
    }

    public void clear() {
        adjacencyList.clear();
    }

    public Collection<Route> getAllEdges() {
        return adjacencyList.values().stream()
                .flatMap(m -> m.values().stream())
                .toList();
    }

    public boolean hasEdge(UUID source, UUID target) {
        return getEdge(source, target).isPresent();
    }
}
