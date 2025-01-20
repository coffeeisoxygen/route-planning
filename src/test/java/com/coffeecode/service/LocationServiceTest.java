package com.coffeecode.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.coffeecode.model.Locations;
import com.coffeecode.repository.LocationRepository;

class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private DistanceService distanceService;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddLocation() {
        Locations location = new Locations("Test Location", 10.0, 20.0);
        when(locationRepository.save(any(Locations.class))).thenReturn(location);

        Locations result = locationService.addLocation("Test Location", 10.0, 20.0);

        assertNotNull(result);
        assertEquals("Test Location", result.getName());
        assertEquals(10.0, result.getLatitude());
        assertEquals(20.0, result.getLongitude());
        verify(locationRepository, times(1)).save(any(Locations.class));
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Locations location = new Locations("Test Location", 10.0, 20.0);
        when(locationRepository.findById(id)).thenReturn(Optional.of(location));

        Optional<Locations> result = locationService.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Test Location", result.get().getName());
        verify(locationRepository, times(1)).findById(id);
    }

    @Test
    void testFindByName() {
        String name = "Test Location";
        Locations location = new Locations(name, 10.0, 20.0);
        when(locationRepository.findByName(name)).thenReturn(Optional.of(location));

        Optional<Locations> result = locationService.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
        verify(locationRepository, times(1)).findByName(name);
    }

    @Test
    void testGetAllLocations() {
        Locations location1 = new Locations("Location 1", 10.0, 20.0);
        Locations location2 = new Locations("Location 2", 30.0, 40.0);
        when(locationRepository.findAll()).thenReturn(Arrays.asList(location1, location2));

        var result = locationService.getAllLocations();

        assertEquals(2, result.size());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void testDeleteLocation() {
        UUID id = UUID.randomUUID();
        doNothing().when(locationRepository).delete(id);

        locationService.deleteLocation(id);

        verify(locationRepository, times(1)).delete(id);
    }

    @Test
    void testLocationExists() {
        UUID id = UUID.randomUUID();
        when(locationRepository.exists(id)).thenReturn(true);

        boolean result = locationService.locationExists(id);

        assertTrue(result);
        verify(locationRepository, times(1)).exists(id);
    }

    @Test
    void testUpdateLocation() {
        UUID id = UUID.randomUUID();
        Locations location = new Locations(id, "Updated Location", 10.0, 20.0);
        when(locationRepository.exists(id)).thenReturn(true);
        when(locationRepository.save(any(Locations.class))).thenReturn(location);

        Locations result = locationService.updateLocation(id, "Updated Location", 10.0, 20.0);

        assertNotNull(result);
        assertEquals("Updated Location", result.getName());
        verify(locationRepository, times(1)).exists(id);
        verify(locationRepository, times(1)).save(any(Locations.class));
    }

    @Test
    void testCalculateDistance() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Locations loc1 = new Locations(id1, "Location 1", 10.0, 20.0);
        Locations loc2 = new Locations(id2, "Location 2", 30.0, 40.0);
        when(locationRepository.findById(id1)).thenReturn(Optional.of(loc1));
        when(locationRepository.findById(id2)).thenReturn(Optional.of(loc2));
        when(distanceService.calculateDistance(loc1, loc2)).thenReturn(100.0);

        double result = locationService.calculateDistance(id1, id2);

        assertEquals(100.0, result);
        verify(locationRepository, times(1)).findById(id1);
        verify(locationRepository, times(1)).findById(id2);
        verify(distanceService, times(1)).calculateDistance(loc1, loc2);
    }
}
