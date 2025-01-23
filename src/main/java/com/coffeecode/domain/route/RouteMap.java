package com.coffeecode.domain.route;

import java.util.Collection;

import java.util.List;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coffeecode.domain.location.model.Locations;
import com.coffeecode.domain.location.service.LocationMap;
import com.coffeecode.domain.location.util.DistanceCalculator;
import com.coffeecode.domain.route.factory.RouteFactory;
import com.coffeecode.domain.route.model.Route;
import com.coffeecode.domain.route.model.RouteType;
import com.coffeecode.domain.graph.*;

@Service
public class RouteMap {

    private final Graph graph;
    private final LocationMap locations;
    private final DistanceCalculator calculator;
    private final GraphOperations graphOps;

    @Autowired
    public RouteMap(DistanceCalculator calculator) {
        this.calculator = calculator;
        this.graph = new Graph();
        this.locations = new LocationMap();
        this.graphOps = new GraphOperations(graph);
    }

    // High-level Route Management
    public void addBidirectionalRoute(UUID sourceId, UUID targetId) {
        double distance = calculateDirectDistance(sourceId, targetId);
        Route[] routes = RouteFactory.createBidirectional(sourceId, targetId, distance, RouteType.DEFAULT);
        for (Route route : routes) {
            graph.addEdge(route);
        }
    }

    public double calculateDirectDistance(UUID from, UUID to) {
        Locations source = locations.getLocation(from).orElseThrow();
        Locations target = locations.getLocation(to).orElseThrow();
        return calculator.calculateDistance(
                source.latitude(), source.longitude(),
                target.latitude(), target.longitude()
        );
    }

    // Location Management
    public void addLocation(Locations location) {
        locations.addLocation(location);
    }

    public Collection<Locations> getLocations() {
        return locations.getLocations();
    }

    public Optional<Locations> getLocation(UUID id) {
        return locations.getLocation(id);
    }

    // Delegate to GraphOperations
    public List<Route> getActiveRoutes(UUID source) {
        return graphOps.getRoutesFrom(source).stream()
                .filter(Route::isActive)
                .collect(Collectors.toList());
    }

    public void clear() {
        locations.clear();
        graph.clear();
    }
}
