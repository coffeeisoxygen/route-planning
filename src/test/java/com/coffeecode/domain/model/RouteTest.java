package com.coffeecode.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.UUID;

class RouteTest {

    @Test
    void createRoute_withValidData_shouldSucceed() {
        UUID source = UUID.randomUUID();
        UUID target = UUID.randomUUID();
        Route route = new Route(source, target, 10.5, Route.RouteType.DIRECT);
        
        assertEquals(source, route.sourceId());
        assertEquals(target, route.targetId());
        assertEquals(10.5, route.distance());
        assertEquals(Route.RouteType.DIRECT, route.type());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -0.1, -100})
    void createRoute_withNegativeDistance_shouldThrowException(double distance) {
        UUID source = UUID.randomUUID();
        UUID target = UUID.randomUUID();
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Route(source, target, distance, Route.RouteType.DIRECT));
    }

    @Test
    void createRoute_withSameSourceAndTarget_shouldThrowException() {
        UUID id = UUID.randomUUID();
        
        assertThrows(IllegalArgumentException.class,
            () -> new Route(id, id, 10.0, Route.RouteType.DIRECT));
    }

    @Test
    void createBidirectionalRoutes_shouldCreateTwoRoutes() {
        UUID source = UUID.randomUUID();
        UUID target = UUID.randomUUID();
        
        Route[] routes = Route.createBidirectional(source, target, 10.0, Route.RouteType.DIRECT);
        
        assertEquals(2, routes.length);
        assertEquals(source, routes[0].sourceId());
        assertEquals(target, routes[0].targetId());
        assertEquals(target, routes[1].sourceId());
        assertEquals(source, routes[1].targetId());
        assertEquals(10.0, routes[0].distance());
        assertEquals(10.0, routes[1].distance());
    }
}