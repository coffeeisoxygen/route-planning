package com.coffeecode.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coffeecode.application.port.output.LocationPersistancePort;
import com.coffeecode.domain.location.model.Locations;
import com.coffeecode.domain.route.model.RouteMap;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationPersistancePort persistancePort;
    @Mock
    private RouteMap routeMap;
    private LocationServiceImpl locationService;
    private Locations testLocation;

    @BeforeEach
    void setUp() {
        locationService = new LocationServiceImpl(persistancePort, routeMap);
        testLocation = new Locations("Test", -6.914744, 107.609810);
    }

    @Test
    void addLocation_shouldSaveAndAddToRouteMap() {
        when(persistancePort.save(any(Locations.class))).thenReturn(testLocation);

        Locations result = locationService.addLocation("Test", -6.914744, 107.609810);

        assertEquals(testLocation, result);
        verify(persistancePort).save(any(Locations.class));
        verify(routeMap).addLocation(testLocation);
    }

    @Test
    void deleteLocation_whenExists_shouldDeleteAndNotify() {
        when(persistancePort.findById(testLocation.id())).thenReturn(Optional.of(testLocation));

        locationService.deleteLocation(testLocation.id());

        verify(persistancePort).delete(testLocation.id());
    }

    @Test
    void updateLocation_whenExists_shouldUpdateBoth() {
        UUID id = testLocation.id();
        when(persistancePort.exists(id)).thenReturn(true);

        locationService.updateLocation(id, "Updated", -6.0, 107.0);

        verify(persistancePort).save(any(Locations.class));
        verify(routeMap).addLocation(any(Locations.class));
    }
}
