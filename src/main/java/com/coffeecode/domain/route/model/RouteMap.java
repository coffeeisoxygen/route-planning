package com.coffeecode.domain.route.model;

import java.util.Collection;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coffeecode.domain.location.model.Locations;
import com.coffeecode.domain.location.service.LocationMap;
import com.coffeecode.domain.location.util.GeoToolsCalculator;
import com.coffeecode.domain.graph.*;

@Service
public class RouteMap {

    private final LocationMap locations;
    private final GraphOperations graphOps;

    @Autowired
    public RouteMap(GeoToolsCalculator calculator, LocationMap locations) {
        this.locations = locations;
        Graph graph = new Graph();
        this.graphOps = new GraphOperations(graph, calculator, locations);
    }

    // High-level coordination only
    public void addBidirectionalRoute(UUID sourceId, UUID targetId) {
        graphOps.addBidirectionalRoute(sourceId, targetId);
    }

    // Location management
    public void addLocation(Locations location) {
        locations.addLocation(location);
    }

    public Collection<Locations> getLocations() {
        return locations.getLocations();
    }

    public Optional<Locations> getLocation(UUID id) {
        return locations.getLocation(id);
    }

    // Clear all data
    public void clear() {
        locations.clear();
        graphOps.clear();
    }
}
