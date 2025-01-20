package com.coffeecode.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coffeecode.model.Locations;
import com.coffeecode.repository.LocationRepository;

@ExtendWith(MockitoExtension.class)
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
        assertEquals("Test Location", result.name());
        assertEquals(10.0, result.latitude());
        assertEquals(20.0, result.longitude());
        verify(locationRepository, times(1)).save(any(Locations.class));
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Locations location = new Locations("Test Location", 10.0, 20.0);
        when(locationRepository.findById(id)).thenReturn(Optional.of(location));

        Optional<Locations> result = locationService.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Test Location", result.get().name());
        verify(locationRepository, times(1)).findById(id);
    }

    @Test
    void testFindByName() {
        String name = "Test Location";
        Locations location = new Locations(name, 10.0, 20.0);
        when(locationRepository.findByName(name)).thenReturn(Optional.of(location));

        Optional<Locations> result = locationService.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().name());
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
        assertEquals("Updated Location", result.name());
        verify(locationRepository, times(1)).exists(id);
        verify(locationRepository, times(1)).save(any(Locations.class));
    }

    @Test
    void testCalculateDistance() {
        // Create fixed UUIDs for consistent testing
        UUID id1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        UUID id2 = UUID.fromString("123e4567-e89b-12d3-a456-556642440001");

        // Create test locations
        Locations loc1 = new Locations(id1, "Location 1", 10.0, 20.0);
        Locations loc2 = new Locations(id2, "Location 2", 30.0, 40.0);
        double expectedDistance = 100.0;

        // Setup mock behavior
        when(locationRepository.findById(id1)).thenReturn(Optional.of(loc1));
        when(locationRepository.findById(id2)).thenReturn(Optional.of(loc2));
        when(distanceService.calculateDistance(any(Locations.class), any(Locations.class)))
                .thenReturn(expectedDistance);

        // Execute and verify
        double result = locationService.calculateDistance(id1, id2);

        assertEquals(expectedDistance, result);
        verify(locationRepository).findById(id1);
        verify(locationRepository).findById(id2);
        verify(distanceService).calculateDistance(loc1, loc2);
    }

    @Test
    void testCalculateDistanceLocationNotFound() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        when(locationRepository.findById(id1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> locationService.calculateDistance(id1, id2));
        assertTrue(exception.getMessage().contains("Location not found"));
    }

    @Test
    void testUpdateLocationNotFound() {
        UUID id = UUID.randomUUID();
        when(locationRepository.exists(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> locationService.updateLocation(id, "Test", 0.0, 0.0));
        assertTrue(exception.getMessage().contains("Location not found"));
    }

    @Test
    void testInvalidCoordinates() {
        String name = "Invalid Location";
        double invalidLat = 91.0;
        double invalidLon = 181.0;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> locationService.addLocation(name, invalidLat, invalidLon));
        assertTrue(exception.getMessage().contains("latitude must be between -90 and 90"));

        // Test invalid longitude
        exception = assertThrows(IllegalArgumentException.class,
                () -> locationService.addLocation(name, 0.0, invalidLon));
        assertTrue(exception.getMessage().contains("longitude must be between -180 and 180"));

        // Test null name
        exception = assertThrows(IllegalArgumentException.class,
                () -> locationService.addLocation(null, 0.0, 0.0));
        assertTrue(exception.getMessage().contains("Name cannot be null or empty"));
    }
}
