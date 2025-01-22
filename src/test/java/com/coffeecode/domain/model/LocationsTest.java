package com.coffeecode.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import com.coffeecode.domain.exception.InvalidLocationException;

class LocationsTest {

    @Test
    void createLocation_withValidData_shouldSucceed() {
        Locations location = new Locations("Bandung", -6.914744, 107.609810);

        assertNotNull(location.id());
        assertEquals("Bandung", location.name());
        assertEquals(-6.914744, location.latitude());
        assertEquals(107.609810, location.longitude());
    }

    @ParameterizedTest
    @CsvSource({
        ", -6.914744, 107.609810",
        "'', -6.914744, 107.609810",
        "'   ', -6.914744, 107.609810"
    })
    void createLocation_withInvalidName_shouldThrowException(
            String name, double lat, double lon) {
        assertThrows(InvalidLocationException.class,
                () -> new Locations(name, lat, lon));
    }

    @ParameterizedTest
    @CsvSource({
        "Bandung, -91, 107.609810",
        "Bandung, 91, 107.609810",
        "Bandung, -6.914744, -181",
        "Bandung, -6.914744, 181"
    })
    void createLocation_withInvalidCoordinates_shouldThrowException(
            String name, double lat, double lon) {
        assertThrows(InvalidLocationException.class,
                () -> new Locations(name, lat, lon));
    }

    @Test
    void toString_shouldReturnFormattedString() {
        Locations location = new Locations("Bandung", -6.914744, 107.609810);
        String result = location.toString();

        assertTrue(result.contains("Bandung"));
        assertTrue(result.contains("-6.914744"));
        assertTrue(result.contains("107.609810"));
        assertTrue(result.contains(location.id().toString().substring(0, 8)));
    }
}
