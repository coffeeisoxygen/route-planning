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

    public List<Route> getNeighbors(UUID locationId) {
        return routes.getOrDefault(locationId, Collections.emptyMap())
                .values()
                .stream()
                .toList();
    }

    public void addRoute(UUID sourceId, UUID targetId, Route.RouteType type) {
        double distance = calculateDirectDistance(sourceId, targetId);
        Route route = new Route(sourceId, targetId, distance, type);
        routes.computeIfAbsent(sourceId, k -> new HashMap<>())
                .put(targetId, route);
    }

    public void addBidirectionalRoute(UUID sourceId, UUID targetId) {
        addRoute(sourceId, targetId, Route.RouteType.DIRECT);
        addRoute(targetId, sourceId, Route.RouteType.DIRECT);
    }

    public Optional<Route> getRoute(UUID source, UUID target) {
        return Optional.ofNullable(routes.get(source))
                .map(m -> m.get(target));
    }

    public void addRoute(UUID sourceId, UUID targetId) {

        double distance = calculateDirectDistance(sourceId, targetId);

        Route route = new Route(sourceId, targetId, distance, Route.RouteType.DIRECT);
        Route reverseRoute = new Route(targetId, sourceId, distance, Route.RouteType.DIRECT);

        routes.computeIfAbsent(sourceId, k -> new HashMap<>()).put(targetId, route);
        routes.computeIfAbsent(targetId, k -> new HashMap<>()).put(sourceId, reverseRoute);

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
}
