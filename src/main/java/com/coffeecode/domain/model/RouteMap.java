package com.coffeecode.domain.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coffeecode.domain.model.Route.RouteType;
import com.coffeecode.domain.util.DistanceCalculator;

@Service
public class RouteMap {

    private final Map<UUID, Locations> locations;
    private final Map<UUID, Map<UUID, Route>> routes;
    private final DistanceCalculator calculator;

    @Autowired
    public RouteMap(DistanceCalculator calculator) {
        this.calculator = calculator;
        this.locations = new HashMap<>();
        this.routes = new HashMap<>();
    }

    // Add helper methods for algorithms
    public List<Route> getRoutesFrom(UUID source) {
        return routes.getOrDefault(source, Collections.emptyMap())
                .values()
                .stream()
                .toList();
    }

    public List<Route> getOutgoingRoutes(UUID source) {
        return routes.getOrDefault(source, Collections.emptyMap())
                .values().stream().toList();
    }

    public List<Route> getIncomingRoutes(UUID target) {
        return routes.entrySet().stream()
                .flatMap(e -> e.getValue().values().stream())
                .filter(r -> r.targetId().equals(target))
                .toList();
    }

    public List<Route> getNeighbors(UUID locationId) {
        return routes.getOrDefault(locationId, Collections.emptyMap())
                .values()
                .stream()
                .toList();
    }

    public double getEdgeWeight(UUID source, UUID target) {
        return getRoute(source, target)
                .map(Route::distance)
                .orElse(Double.POSITIVE_INFINITY);
    }

    public void addWeightedRoute(UUID source, UUID target, double weight, RouteType type) {
        Route route = Route.builder()
                .sourceId(source)
                .targetId(target)
                .distance(weight)
                .weight(weight)
                .type(type)
                .build();
        routes.computeIfAbsent(source, k -> new HashMap<>())
                .put(target, route);
    }

    // Graph modification
    public void removeRoute(UUID source, UUID target) {
        if (routes.containsKey(source)) {
            routes.get(source).remove(target);
        }
    }

    public void addRoute(UUID sourceId, UUID targetId, Route.RouteType type) {
        double distance = calculateDirectDistance(sourceId, targetId);
        Route route = Route.create(sourceId, targetId, distance, type);
        routes.computeIfAbsent(sourceId, k -> new HashMap<>())
                .put(targetId, route);
    }

    public void addBidirectionalRoute(UUID sourceId, UUID targetId) {
        Route[] bidirectionalRoutes = Route.createBidirectional(sourceId, targetId,
                calculateDirectDistance(sourceId, targetId),
                Route.RouteType.DIRECT);
        for (Route route : bidirectionalRoutes) {
            this.routes.computeIfAbsent(route.sourceId(), k -> new HashMap<>())
                    .put(route.targetId(), route);
        }
    }

    public Optional<Route> getRoute(UUID source, UUID target) {
        return Optional.ofNullable(routes.get(source))
                .map(m -> m.get(target));
    }

    public void addLocation(Locations location) {
        locations.put(location.id(), location);
    }

    public Collection<Locations> getLocations() {
        return Collections.unmodifiableCollection(locations.values());
    }

    public Collection<Route> getRoutes() {
        return routes.values().stream()
                .flatMap(m -> m.values().stream())
                .toList();
    }

    public Optional<Double> getDistance(UUID from, UUID to) {
        return getRoute(from, to).map(Route::distance);
    }

    public double calculateDirectDistance(UUID from, UUID to) {
        Locations fromLoc = locations.get(from);
        Locations toLoc = locations.get(to);

        if (fromLoc == null || toLoc == null) {
            throw new IllegalArgumentException("Locations not found");
        }

        return calculator.calculateDistance(
                fromLoc.latitude(), fromLoc.longitude(),
                toLoc.latitude(), toLoc.longitude()
        );
    }

    public Optional<Locations> getLocation(UUID id) {
        return Optional.ofNullable(locations.get(id));
    }

    public boolean hasRoute(UUID sourceId, UUID targetId) {

        return getRoutes().stream()
                .anyMatch(route -> route.getSourceId().equals(sourceId) && route.getTargetId().equals(targetId));
    }

    public void clear() {
        locations.clear();
        routes.clear();
    }

    // Path validation
    public boolean isValidPath(List<Route> path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        for (int i = 0; i < path.size() - 1; i++) {
            Route current = path.get(i);
            Route next = path.get(i + 1);
            if (!current.targetId().equals(next.sourceId())) {
                return false;
            }
        }
        return true;
    }

    // Calculate total path weight
    public double calculatePathWeight(List<Route> path) {
        return path.stream()
                .filter(r -> r.status() == Route.RouteStatus.ACTIVE)
                .mapToDouble(Route::weight)
                .sum();
    }

    // Get all active routes
    public List<Route> getActiveRoutes(UUID source) {
        return getOutgoingRoutes(source).stream()
                .filter(r -> r.status() == Route.RouteStatus.ACTIVE)
                .toList();
    }

    // Find route with minimum weight
    public Optional<Route> findMinWeightRoute(UUID source, UUID target) {
        return getOutgoingRoutes(source).stream()
                .filter(r -> r.targetId().equals(target))
                .filter(r -> r.status() == Route.RouteStatus.ACTIVE)
                .min((r1, r2) -> Double.compare(r1.weight(), r2.weight()));
    }
}
