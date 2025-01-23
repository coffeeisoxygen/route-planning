package com.coffeecode.domain.model;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RouteTest {

    private final UUID source = UUID.randomUUID();
    private final UUID target = UUID.randomUUID();
    private final double validDistance = 10.5;

    @Test
    void builder_withValidData_shouldCreateRoute() {
        Route route = Route.builder()
                .sourceId(source)
                .targetId(target)
                .distance(validDistance)
                .type(Route.RouteType.DIRECT)
                .build();

        assertEquals(source, route.sourceId());
        assertEquals(target, route.targetId());
        assertEquals(validDistance, route.distance());
        assertEquals(Route.RouteType.DIRECT, route.type());
        assertEquals(Route.RouteStatus.ACTIVE, route.status());
        assertNotNull(route.metadata());
    }

    @Test
    void builder_withCustomWeight_shouldSetWeight() {
        double customWeight = 15.0;
        Route route = Route.builder()
                .sourceId(source)
                .targetId(target)
                .distance(validDistance)
                .weight(customWeight)
                .type(Route.RouteType.CALCULATED)
                .build();

        assertEquals(customWeight, route.weight());
    }

    @Test
    void builder_withStatus_shouldSetStatus() {
        Route route = Route.builder()
                .sourceId(source)
                .targetId(target)
                .distance(validDistance)
                .type(Route.RouteType.DIRECT)
                .status(Route.RouteStatus.UNDER_MAINTENANCE)
                .build();

        assertEquals(Route.RouteStatus.UNDER_MAINTENANCE, route.status());
        assertFalse(route.isActive());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -0.1, -100})
    void builder_withNegativeDistance_shouldThrowException(double distance) {
        assertThrows(IllegalArgumentException.class, ()
                -> Route.builder()
                        .sourceId(source)
                        .targetId(target)
                        .distance(distance)
                        .type(Route.RouteType.DIRECT)
                        .build());
    }

    @Test
    void builder_withSameSourceAndTarget_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()
                -> Route.builder()
                        .sourceId(source)
                        .targetId(source)
                        .distance(validDistance)
                        .type(Route.RouteType.DIRECT)
                        .build());
        assertEquals("Source and target cannot be the same", exception.getMessage());
    }

    @Test
    void createBidirectional_shouldCreateTwoRoutes() {
        Route[] routes = Route.createBidirectional(source, target, validDistance, Route.RouteType.DIRECT);

        assertEquals(2, routes.length);
        assertEquals(source, routes[0].sourceId());
        assertEquals(target, routes[0].targetId());
        assertEquals(target, routes[1].sourceId());
        assertEquals(source, routes[1].targetId());
    }

    @Test
    void toString_shouldFormatCorrectly() {
        Route route = Route.create(source, target, validDistance, Route.RouteType.DIRECT);
        String routeString = route.toString();

        assertTrue(routeString.contains(source.toString().substring(0, 8)));
        assertTrue(routeString.contains(target.toString().substring(0, 8)));
        assertTrue(routeString.contains(String.format("%.2f", validDistance)));
    }
}
