package com.coffeecode.application.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coffeecode.domain.exception.RouteNotFoundException;
import com.coffeecode.domain.route.RouteMap;
import com.coffeecode.domain.route.model.Route;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    private RouteMap routeMap;
    private RouteServiceImpl routeService;
    private UUID source;
    private UUID target;

    @BeforeEach
    void setUp() {
        routeService = new RouteServiceImpl(routeMap);
        source = UUID.randomUUID();
        target = UUID.randomUUID();
    }

    @Test
    void getRoute_whenExists_shouldReturnRoute() {
        Route expectedRoute = new Route(source, target, 100.0, 100.0, Route.RouteStatus.ACTIVE,
                new Route.RouteMetadata(0L, 0L, ""), Route.RouteType.DIRECT);
        when(routeMap.getRoute(source, target)).thenReturn(Optional.of(expectedRoute));

        Route result = routeService.getRoute(source, target);

        assertEquals(expectedRoute, result);
    }

    @Test
    void getRoute_whenNotExists_shouldThrowException() {
        when(routeMap.getRoute(source, target)).thenReturn(Optional.empty());

        assertThrows(RouteNotFoundException.class, () -> routeService.getRoute(source, target));
    }

    @Test
    void getAllRoutes_shouldReturnAllRoutes() {
        Collection<Route> expectedRoutes = List.of(
                new Route(source, target, 100.0, 100.0, Route.RouteStatus.ACTIVE, new Route.RouteMetadata(0L, 0L, ""),
                        Route.RouteType.DIRECT));
        when(routeMap.getRoutes()).thenReturn(expectedRoutes);

        Collection<Route> result = routeService.getAllRoutes();

        assertEquals(expectedRoutes, result);
    }
}
