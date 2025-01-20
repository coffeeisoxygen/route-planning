package com.coffeecode.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class LocationsTest {

    @Test
    void constructor_ValidInput_CreatesLocation() {
        Locations location = new Locations("Jakarta", -6.200000, 106.816666);
        assertNotNull(location.id());
        assertEquals("Jakarta", location.name());
        assertEquals(-6.200000, location.latitude());
        assertEquals(106.816666, location.longitude());
    }

    @Test
    void constructor_InvalidLatitude_ThrowsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> new Locations("Test", 91.0, 0.0));
    }

    @Test
    void constructor_InvalidLongitude_ThrowsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> new Locations("Test", 0.0, 181.0));
    }

    @Test
    void constructor_EmptyName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> new Locations("", 0.0, 0.0));
    }
}
