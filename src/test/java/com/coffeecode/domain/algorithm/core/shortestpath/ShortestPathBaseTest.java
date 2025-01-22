package com.coffeecode.domain.algorithm.core.shortestpath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;

import com.coffeecode.domain.algorithm.strategy.BasePathFindingTest;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.domain.model.Route;

public abstract class ShortestPathBaseTest extends BasePathFindingTest {

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        testLocations = new HashMap<>();
        setupTestLocations();
        setupRoutes();
    }

    private void setupTestLocations() {
        createLocation("A", 0.0, 0.0);
        createLocation("B", 1.0, 1.0);
        createLocation("C", 2.0, 2.0);
        createLocation("D", 0.0, 3.0);
        createLocation("E", 1.0, 3.0);
        createLocation("F", 2.0, 3.0);
        createLocation("G", 0.0, 6.0);
        createLocation("H", 1.0, 6.0);
        createLocation("I", 2.0, 6.0);

        testLocations.values().forEach(routeMap::addLocation);
    }

    private void setupRoutes() {
        // Grid connections
        addBidirectionalRoute("A", "B");
        addBidirectionalRoute("B", "C");
        addBidirectionalRoute("D", "E");
        addBidirectionalRoute("E", "F");
        addBidirectionalRoute("G", "H");
        addBidirectionalRoute("H", "I");

        // Vertical connections
        addBidirectionalRoute("A", "D");
        addBidirectionalRoute("B", "E");
        addBidirectionalRoute("C", "F");
        addBidirectionalRoute("D", "G");
        addBidirectionalRoute("E", "H");
        addBidirectionalRoute("F", "I");
    }

    private void addBidirectionalRoute(String from, String to) {
        Locations source = testLocations.get(from);
        Locations target = testLocations.get(to);

        double distance = routeMap.calculateDirectDistance(source.id(), target.id());
        routeMap.addRoute(source.id(), target.id());
        routeMap.addRoute(target.id(), source.id());
    }

    protected boolean isShortestPath(List<Route> path, UUID source, UUID target) {
        if (path.isEmpty()) {
            return false;
        }

        double pathDistance = calculateTotalDistance(path);
        double directDistance = routeMap.calculateDirectDistance(source, target);

        return pathDistance <= directDistance * 1.1; // Within 10% of direct
    }

    protected void verifyPathProperties(List<Route> path, UUID source, UUID target) {
        assertFalse(path.isEmpty(), "Path should not be empty");
        assertTrue(isPathConnected(path), "Path should be connected");
        assertEquals(source, path.get(0).sourceId(), "Path should start at source");
        assertEquals(target, path.get(path.size() - 1).targetId(), "Path should end at target");
    }

    protected void verifyAllPairsDistances(Map<UUID, Map<UUID, Double>> distances) {
        int expectedSize = testLocations.size();
        assertEquals(expectedSize, distances.size(), "Should have distances for all vertices");

        distances.values().forEach(distMap
                -> assertEquals(expectedSize, distMap.size(), "Each vertex should have distances to all others")
        );
    }
}
