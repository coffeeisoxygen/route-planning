package com.coffeecode.domain.model;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coffeecode.domain.util.DistanceCalculator;

@ExtendWith(MockitoExtension.class)
class RouteMapTest {

    @Mock
    private DistanceCalculator calculator;

    private RouteMap routeMap;
    private Locations bandung;
    private Locations jakarta;
    private Locations surabaya;

    @BeforeEach
    void setUp() {
        routeMap = new RouteMap(calculator);
        bandung = new Locations("Bandung", -6.914744, 107.609810);
        jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        surabaya = new Locations("Surabaya", -7.250445, 112.768845);
    }

    @Test
    void addLocation_whenEmpty_shouldAddWithoutRoutes() {
        routeMap.addLocation(bandung);

        assertEquals(1, routeMap.getLocations().size());
        assertTrue(routeMap.getRoutes().isEmpty());
    }

    @Test
    void addLocation_withExisting_shouldCreateBidirectionalRoutes() {
        // Use any() matcher for Locations parameters
        when(calculator.calculateDistance(
            any(Locations.class), 
            any(Locations.class)
        )).thenReturn(100.0);

        routeMap.addLocation(bandung);
        routeMap.addLocation(jakarta);

        Collection<Route> routes = routeMap.getRoutes();
        assertEquals(2, routes.size());

        // Verify calculator was called at least once
        verify(calculator, atLeastOnce())
            .calculateDistance(any(Locations.class), any(Locations.class));
    }

    @Test
    void addLocation_withMultiple_shouldCreateFullyConnectedGraph() {
        // Use any() matcher for all calculator calls
        when(calculator.calculateDistance(
            any(Locations.class), 
            any(Locations.class)
        )).thenReturn(100.0);

        routeMap.addLocation(bandung);
        routeMap.addLocation(jakarta);
        routeMap.addLocation(surabaya);

        assertEquals(3, routeMap.getLocations().size());
        assertEquals(6, routeMap.getRoutes().size());

        // Verify calculator was called for each pair
        verify(calculator, times(3))
            .calculateDistance(any(Locations.class), any(Locations.class));
    }

    @Test
    void getRoute_withValidRoute_shouldReturnRoute() {
        // Stub both directions
        when(calculator.calculateDistance(any(Locations.class), any(Locations.class)))
                .thenReturn(100.0);

        routeMap.addLocation(bandung);
        routeMap.addLocation(jakarta);

        Optional<Route> route = routeMap.getRoute(bandung.id(), jakarta.id());

        assertTrue(route.isPresent());
        assertEquals(100.0, route.get().distance());
        assertEquals(Route.RouteType.DIRECT, route.get().type());

        // Verify calculator was called
        verify(calculator).calculateDistance(any(Locations.class), any(Locations.class));
    }
}
