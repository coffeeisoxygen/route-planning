package com.coffeecode.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.coffeecode.domain.exception.InvalidLocationException;

class LocationsTest {

    @Test
    void shouldCreateLocationWithValidParameters() {
        Locations location = new Locations("Test Location", 0, 0);
        assertNotNull(location.id());
        assertEquals("Test Location", location.name());
        assertEquals(0, location.latitude());
        assertEquals(0, location.longitude());
    }

    @Test
    void shouldThrowExceptionForNullName() {
        assertThrows(InvalidLocationException.class,
                () -> new Locations(null, 0, 0));
    }

    @ParameterizedTest
    @CsvSource({
        "'', 0, 0, Name cannot be null or empty",
        "'  ', 0, 0, Name cannot be null or empty",
        "Test, -91, 0, latitude must be between -90 and 90",
        "Test, 91, 0, latitude must be between -90 and 90",
        "Test, 0, -181, longitude must be between -180 and 180",
        "Test, 0, 181, longitude must be between -180 and 180"
    })
    void shouldThrowExceptionForInvalidParameters(String name, double lat, double lon, String expectedMessage) {
        InvalidLocationException exception = assertThrows(InvalidLocationException.class,
                () -> new Locations(name, lat, lon));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldCalculateDistanceCorrectly() {
        Locations jakarta = new Locations("Jakarta", -6.200000, 106.816666);
        Locations bandung = new Locations("Bandung", -6.914744, 107.609810);

        double distance = jakarta.distanceTo(bandung.latitude(), bandung.longitude());
        assertTrue(distance > 118 && distance < 130);
    }
}
