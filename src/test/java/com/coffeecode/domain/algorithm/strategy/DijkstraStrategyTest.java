package com.coffeecode.domain.algorithm.strategy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;
import com.coffeecode.domain.model.RouteMap;
import com.coffeecode.domain.util.DistanceCalculator;
import com.coffeecode.infrastructure.distance.GeoToolsCalculator;

class DijkstraStrategyTest {
    private DijkstraStrategy dijkstra;
    private RouteMap routeMap;
    private Locations loc1, loc2, loc3;
    

    @BeforeEach
    void setUp() {
        dijkstra = new DijkstraStrategy();
        DistanceCalculator distanceCalculator = new GeoToolsCalculator();
        routeMap = new RouteMap(distanceCalculator);
        
        loc1 = new Locations("A", 0, 0);
        loc2 = new Locations("B", 1, 1);
        loc3 = new Locations("C", 2, 2);
        
        routeMap.addLocation(loc1);
        routeMap.addLocation(loc2);
        routeMap.addLocation(loc3);
    }
    
    @Test
    void findPath_shouldReturnShortestPath() {
        List<Route> path = dijkstra.findPath(routeMap, loc1.id(), loc3.id());
        
        assertFalse(path.isEmpty());
        assertEquals(loc1.id(), path.get(0).sourceId());
        assertEquals(loc3.id(), path.get(path.size()-1).targetId());
        
        // Verify it's shortest path by checking total distance
        double totalDistance = path.stream()
            .mapToDouble(Route::distance)
            .sum();
            
        Optional<Route> directRoute = routeMap.getRoute(loc1.id(), loc3.id());
        assertTrue(directRoute.isPresent());
        assertEquals(directRoute.get().distance(), totalDistance, 0.001);
    }
    
    @Test
    void findPath_shouldReturnEmpty_whenNoPath() {
        List<Route> path = dijkstra.findPath(routeMap, loc1.id(), UUID.randomUUID());
        assertTrue(path.isEmpty());
    }
}
