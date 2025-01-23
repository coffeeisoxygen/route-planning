package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;

import com.coffeecode.domain.location.model.Locations;
import com.coffeecode.domain.location.util.GeoToolsCalculator;
import com.coffeecode.domain.route.RouteMap;
import com.coffeecode.domain.route.model.Route;

public abstract class BaseShortestPathTest {

    protected RouteMap routeMap;
    protected Locations jakarta, bandung, surabaya, semarang;
    protected static final double EXPECTED_SHORTEST_DISTANCE = 668.78;
    protected static final double DELTA = 0.01;

    @BeforeEach
    protected void setUp() {
        routeMap = new RouteMap(new GeoToolsCalculator());
        initializeLocations();
        createRoutes();
        verifySetup();
    }

    protected void verifySetup() {
        assertEquals(4, routeMap.getLocations().size(), "Should have 4 locations");
        assertTrue(routeMap.getRoutesFrom(jakarta.id()).size() > 0, "Jakarta should have routes");
    }

    private void initializeLocations() {
        jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        bandung = new Locations("Bandung", -6.914744, 107.609810);
        surabaya = new Locations("Surabaya", -7.250445, 112.768845);
        semarang = new Locations("Semarang", -6.966667, 110.416664);

        routeMap.addLocation(jakarta);
        routeMap.addLocation(bandung);
        routeMap.addLocation(surabaya);
        routeMap.addLocation(semarang);
    }

    private void createRoutes() {
        routeMap.addBidirectionalRoute(jakarta.id(), bandung.id());
        routeMap.addBidirectionalRoute(bandung.id(), surabaya.id());
        routeMap.addBidirectionalRoute(jakarta.id(), semarang.id());
        routeMap.addBidirectionalRoute(semarang.id(), surabaya.id());
    }

    protected void verifyShortestPath(List<Route> path) {
        assertNotNull(path, "Path should not be null");
        assertFalse(path.isEmpty(), "Path should not be empty");
        assertEquals(EXPECTED_SHORTEST_DISTANCE, calculatePathDistance(path), DELTA);
    }

    protected void verifyCircularPath(List<Route> path) {
        assertFalse(path.isEmpty(), "Circular path should not be empty");
        assertEquals(path.get(0).sourceId(), path.get(path.size() - 1).targetId(),
                "Path should end at starting point");

        // Verify all nodes in path are connected
        for (int i = 0; i < path.size() - 1; i++) {
            assertEquals(path.get(i).targetId(), path.get(i + 1).sourceId(),
                    "Path should be continuous");
        }
    }

    protected double calculatePathDistance(List<Route> path) {
        return path.stream()
                .mapToDouble(Route::distance)
                .sum();
    }
}
