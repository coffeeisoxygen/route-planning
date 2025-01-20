package com.coffeecode.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LocationsTest {

    @Test
    void constructor_ValidInput_CreatesLocation() {
        Locations location = new Locations("Jakarta", -6.200000, 106.816666);
        assertNotNull(location.id());
        assertEquals("Jakarta", location.name());
        assertEquals(-6.200000, location.latitude());
        assertEquals(106.816666, location.longitude());
    }
    @ParameterizedTest
    @CsvSource({
        // city1, city2, lat1, lon1, lat2, lon2, expectedKm
        "Jakarta, Bandung, -6.200000, 106.816666, -6.914744, 107.609810, 126.7",
        "Jakarta, Surabaya, -6.200000, 106.816666, -7.249720, 112.750830, 692.3",
        "Bandung, Semarang, -6.914744, 107.609810, -6.966667, 110.416664, 308.2"
    })
    void distanceTo_KnownLocations_ReturnsExpectedDistance(
            String city1, String city2, 
            double lat1, double lon1, 
            double lat2, double lon2, 
            double expectedKm) {
        Locations loc1 = new Locations(city1, lat1, lon1);
        Locations loc2 = new Locations(city2, lat2, lon2);
        
        double distance = loc1.distanceTo(loc2);
        
        assertEquals(expectedKm, distance, 0.1); // 100m tolerance
    }

    @Test
    void distanceTo_NullLocation_ThrowsException() {
        Locations jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        assertThrows(IllegalArgumentException.class, () -> jakarta.distanceTo(null));
    }

    @Test
    void distanceTo_SameLocation_ReturnsZero() {
        Locations loc = new Locations("Test", 0.0, 0.0);
        assertEquals(0.0, loc.distanceTo(loc), 0.001);
    }

    @Test
    void constructor_InvalidLatitude_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Locations("Test", 91.0, 0.0));
    }

    @Test
    void constructor_InvalidLongitude_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Locations("Test", 0.0, 181.0));
    }

    @Test
    void constructor_EmptyName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Locations("", 0.0, 0.0));
    }
}