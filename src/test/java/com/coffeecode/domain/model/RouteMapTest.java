package com.coffeecode.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.coffeecode.domain.util.DistanceCalculator;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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
        when(calculator.calculateDistance(bandung, jakarta)).thenReturn(100.0);
        when(calculator.calculateDistance(jakarta, bandung)).thenReturn(100.0);

        routeMap.addLocation(bandung);
        routeMap.addLocation(jakarta);

        Collection<Route> routes = routeMap.getRoutes();
        assertEquals(2, routes.size());

        verify(calculator).calculateDistance(bandung, jakarta);
    }

    @Test
    void addLocation_withMultiple_shouldCreateFullyConnectedGraph() {
        // Setup calculator for each pair
        when(calculator.calculateDistance(bandung, jakarta)).thenReturn(100.0);
        when(calculator.calculateDistance(bandung, surabaya)).thenReturn(150.0);
        when(calculator.calculateDistance(jakarta, surabaya)).thenReturn(200.0);

        routeMap.addLocation(bandung);
        routeMap.addLocation(jakarta);
        routeMap.addLocation(surabaya);

        assertEquals(3, routeMap.getLocations().size());
        assertEquals(6, routeMap.getRoutes().size());

        verify(calculator, times(3)).calculateDistance(any(), any());
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
