package com.coffeecode.domain.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

    public void addLocation(Locations location) {
        locations.put(location.id(), location);
        // Create fully connected graph
        if (locations.size() > 1) {
            locations.values().stream()
                    .filter(l -> !l.equals(location))
                    .forEach(l -> {
                        double distance = calculator.calculateDistance(location, l);
                        Route[] newRoutes = Route.createBidirectional(
                                location.id(), l.id(),
                                distance, Route.RouteType.DIRECT);
                        addRoutes(newRoutes);
                    });
        }
    }

    private void addRoutes(Route... newRoutes) {
        for (Route route : newRoutes) {
            routes.computeIfAbsent(route.sourceId(), k -> new HashMap<>())
                    .put(route.targetId(), route);
        }
    }

    public Collection<Locations> getLocations() {
        return Collections.unmodifiableCollection(locations.values());
    }

    public Collection<Route> getRoutes() {
        return routes.values().stream()
                .flatMap(m -> m.values().stream())
                .toList();
    }

    public Optional<Route> getRoute(UUID source, UUID target) {
        return Optional.ofNullable(routes.get(source))
                .map(m -> m.get(target));
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
}
