package com.coffeecode.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coffeecode.application.port.output.LocationPersistancePort;
import com.coffeecode.application.port.output.RouteCalculationPort;
import com.coffeecode.domain.model.Locations;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationPersistancePort persistancePort;
    @Mock
    private RouteCalculationPort routeCalculationPort;

    private LocationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new LocationServiceImpl(persistancePort, routeCalculationPort);
    }

    @Test
    void shouldAddLocation() {
        String name = "Test Location";
        double lat = 0, lon = 0;

        when(persistancePort.save(any(Locations.class)))
                .thenAnswer(i -> i.getArgument(0));

        Locations result = service.addLocation(name, lat, lon);

        assertNotNull(result);
        assertEquals(name, result.name());
        assertEquals(lat, result.latitude());
        assertEquals(lon, result.longitude());

        verify(persistancePort).save(any(Locations.class));
    }
}
