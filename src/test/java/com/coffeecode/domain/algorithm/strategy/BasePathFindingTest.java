package com.coffeecode.domain.algorithm.strategy;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

public abstract class BasePathFindingTest {

    protected RouteMap routeMap;
    protected Map<String, Locations> testLocations;
    protected Map<UUID, String> idToName;

    @BeforeEach
    void setUp() {
        routeMap = new RouteMap(new GeoToolsCalculator());
        testLocations = new HashMap<>();
        idToName = new HashMap<>();

        // Create 3x3 grid of locations
        createLocation("A", 0, 0);
        createLocation("B", 1, 1);
        createLocation("C", 2, 2);
        createLocation("D", 3, 3);
        createLocation("E", 4, 4);
        createLocation("F", 5, 5);
        createLocation("G", 6, 6);
        createLocation("H", 7, 7);
        createLocation("I", 8, 8);

        // Add all locations to routeMap
        testLocations.values().forEach(loc -> {
            routeMap.addLocation(loc);
            idToName.put(loc.id(), loc.name());
        });
    }

    private void createLocation(String name, double lat, double lon) {
        Locations location = new Locations(name, lat, lon);
        testLocations.put(name, location);
    }

    protected void printRoutes(Collection<Route> routes) {
        System.out.println("\nAll routes:");
        routes.forEach(r
                -> System.out.println(String.format("%s -> %s: %.2f km",
                        idToName.get(r.sourceId()),
                        idToName.get(r.targetId()),
                        r.distance())));
    }

    protected double calculateTotalDistance(List<Route> path) {
        return path.stream()
                .mapToDouble(Route::distance)
                .sum();
    }

    protected boolean isPathConnected(List<Route> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            if (!path.get(i).targetId().equals(path.get(i + 1).sourceId())) {
                return false;
            }
        }
        return true;
    }
}
